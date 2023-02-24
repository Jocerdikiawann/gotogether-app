package com.example.livetracking.ui.page.dashboard

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.livetracking.R
import com.example.livetracking.ui.component.card.CardMap
import com.example.livetracking.ui.component.topbar.ProfileBar
import com.example.livetracking.ui.theme.BlueBg
import com.example.livetracking.ui.theme.BlueHyperlink
import com.example.livetracking.ui.theme.GrayBG
import com.example.livetracking.ui.theme.LiveTrackingTheme
import com.example.livetracking.utils.PermissionUtils
import com.example.livetracking.utils.from
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.fade
import com.google.accompanist.placeholder.placeholder
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import java.util.*


data class DashboardStateUI(
    val lat: Double = -6.2881792,
    val lng: Double = 106.7614208,
)

data class AddressStateUI(
    val loading: Boolean = false,
    val error: Boolean = false,
    val errMsg: String = "",
    val addressFirst: String = "empty address",
    val addressSecond: String = "empty address"
)

data class LocationStateUI(
    val permission: Boolean = false,
    val isGpsOn: Boolean = false,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageDashboard(
    modifier: Modifier = Modifier,
    nav: NavHostController
) {
    val ctx = LocalContext.current
    val viewModel = hiltViewModel<ViewModelDashboard>()
    val scope = rememberCoroutineScope()
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current

    val havePermission by viewModel.havePermission.observeAsState(LocationStateUI())
    val dashboardStateUI by viewModel.getLocation().observeAsState(DashboardStateUI())
    val addressStateUI by viewModel.addressStateUI.observeAsState(AddressStateUI())
    var mapsReady by remember { mutableStateOf(false) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(
                dashboardStateUI.lat,
                dashboardStateUI.lng
            ), 18f
        )
    }

    val mapsUiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                compassEnabled = false,
                zoomControlsEnabled = false,
                myLocationButtonEnabled = true
            )
        )
    }

    val permissionUtils = PermissionUtils(ctx)
    val permissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestMultiplePermissions()) {
            viewModel.havePermission()
        }

    fun updateUiAndLocation() {
        scope.launch {
            cameraPositionState.animate(
                update = CameraUpdateFactory.newCameraPosition(
                    CameraPosition(
                        LatLng(
                            dashboardStateUI.lat,
                            dashboardStateUI.lng
                        ), 18f, 0f, 0f
                    ),
                ),
                durationMs = 1000
            )
            withTimeoutOrNull(15000) {
                viewModel.getAddress(dashboardStateUI.lat, dashboardStateUI.lng)
            }
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                viewModel.havePermission()
                if (!havePermission.isGpsOn) {
                    viewModel.turnOnGps(ctx as Activity)
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    DisposableEffect(dashboardStateUI) {
        if (mapsReady) {
            updateUiAndLocation()
        }
        onDispose { }
    }


    Scaffold(
        topBar = {
            ProfileBar(
                name = "Jon Doe"
            )
        },
        bottomBar = {},
    ) {
        LazyColumn(modifier = modifier.padding(it), content = {
            if (!havePermission.permission) {
                item {
                    Column(
                        modifier = modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_permission_not_given),
                            contentDescription = "ic_permission",
                            modifier = modifier
                                .width(250.dp.from(ctx))
                                .height(250.dp.from(ctx)),
                            tint = BlueHyperlink
                        )
                        Text(
                            text = "Please give permission and turn on GPS\nto access the application features.\uD83D\uDE15",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp.from(ctx),
                                color = Color.Black
                            ),
                            textAlign = TextAlign.Center
                        )
                        Button(onClick = {
                            permissionLauncher.launch(permissionUtils.listPermission())
                            Log.e("err", permissionUtils.listPermission().toString())
                        }, colors = ButtonDefaults.buttonColors(containerColor = BlueHyperlink)) {
                            Text(
                                text = "Get Started",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 12.sp.from(ctx),
                                    color = Color.White
                                )
                            )
                        }

                    }
                }
            } else {
                item {
                    Row(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp.from(ctx)),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Where are you going\ntoday?",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp.from(ctx),
                                color = Color.Black,
                            )
                        )

                        Box(
                            modifier = modifier
                                .width(30.dp.from(ctx))
                                .height(30.dp.from(ctx))
                                .clip(CircleShape)
                                .background(GrayBG),
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_search),
                                contentDescription = "ic_search",
                                tint = Color.Black,
                                modifier = modifier.padding(5.dp.from(ctx))
                            )
                        }
                    }
                }
                item {
                    Row(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(
                                start = 12.dp.from(ctx),
                                end = 12.dp.from(ctx),
                                top = 25.dp.from(ctx),
                                bottom = 12.dp.from(ctx)
                            ),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Your Location",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp.from(ctx),
                                color = Color.Black,
                            )
                        )
                        Text(
                            text = "View Full Map",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp.from(ctx),
                                color = BlueHyperlink,
                            )
                        )
                    }
                    CardMap(
                        ctx = ctx,
                        latLng = LatLng(dashboardStateUI.lat, dashboardStateUI.lng),
                        cameraPositionState = cameraPositionState,
                        mapsUiSettings = mapsUiSettings,
                        onMapLoaded = {
                            mapsReady = true
                            updateUiAndLocation()
                        },
                        onMyLocationButtonClick = {
                            Log.e("myLoc", "${it.latitude},${it.longitude}")
                        },
                        googleMapOptions = {
                            GoogleMapOptions().camera(
                                CameraPosition.fromLatLngZoom(
                                    LatLng(
                                        dashboardStateUI.lat,
                                        dashboardStateUI.lng
                                    ),
                                    18f
                                )
                            )
                        }
                    )
                    Spacer(modifier = modifier.height(12.dp.from(ctx)))
                    Row(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp.from(ctx)),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = modifier
                                .width(30.dp.from(ctx))
                                .height(30.dp.from(ctx))
                                .clip(CircleShape)
                                .background(BlueBg)
                                .placeholder(
                                    visible = addressStateUI.loading,
                                    highlight = PlaceholderHighlight.fade(),
                                    shape = CircleShape,
                                    color = Color.Gray,
                                ),
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_location_on_24),
                                contentDescription = "ic_marker",
                                tint = Color.White,
                                modifier = modifier.padding(5.dp.from(ctx))
                            )
                        }
                        Spacer(modifier = modifier.width(10.dp.from(ctx)))
                        Text(modifier = modifier.placeholder(
                            visible = addressStateUI.loading,
                            highlight = PlaceholderHighlight.fade(),
                            shape = RoundedCornerShape(8.dp.from(ctx)),
                            color = Color.Gray,
                        ),
                            text = buildAnnotatedString {
                                withStyle(
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 12.sp.from(ctx),
                                        color = Color.Black,
                                    ).toSpanStyle()
                                ) {
                                    append(
                                        "${addressStateUI.addressFirst.uppercase()},"
                                    )
                                }
                                withStyle(
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 12.sp.from(ctx),
                                        color = Color.Gray,
                                    ).toSpanStyle()
                                ) {
                                    append(addressStateUI.addressSecond.uppercase())
                                }
                            })
                    }
                }
                item {
                    Column(
                        modifier = modifier
                            .fillMaxWidth()
                            .clip(
                                RoundedCornerShape(
                                    topEnd = 10.dp.from(ctx),
                                    topStart = 10.dp.from(ctx)
                                )
                            )
                            .background(color = GrayBG)
                    ) {
                            Row() {
                                
                            }
                    }
                }
            }
        })
    }
}

@Composable
@Preview(showSystemUi = true)
fun DisplayDashboard() {
    LiveTrackingTheme {
        PageDashboard(nav = rememberNavController())
    }
}