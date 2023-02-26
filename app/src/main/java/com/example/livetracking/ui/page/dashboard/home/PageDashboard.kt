package com.example.livetracking.ui.page.dashboard.home

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.example.livetracking.R
import com.example.livetracking.ui.component.card.CardMap
import com.example.livetracking.ui.theme.GrayBG
import com.example.livetracking.ui.theme.LiveTrackingTheme
import com.example.livetracking.ui.theme.Primary
import com.example.livetracking.ui.theme.Secondary
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

object Dashboard {
    const val routeName = "dashboard"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageDashboard(
    modifier: Modifier = Modifier,
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
//            withTimeoutOrNull(15000) {
//                viewModel.getAddress(dashboardStateUI.lat, dashboardStateUI.lng)
//            }
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

    LazyColumn(modifier = modifier.fillMaxWidth(), content = {
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
                        tint = Primary
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
                    }, colors = ButtonDefaults.buttonColors(containerColor = Secondary)) {
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
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = {
                        Text(
                            text = "Find your destination... ",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 12.sp.from(ctx),
                                fontWeight = FontWeight.Normal,
                                color = Color.Gray
                            )
                        )
                    },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.search),
                            contentDescription = "ic_search",
                            tint = Color.Gray,
                            modifier = modifier
                                .width(30.dp.from(ctx))
                                .height(30.dp.from(ctx)),

                            )
                    },
                    modifier = modifier.fillMaxWidth(),
                    readOnly = true,
                    enabled = false
                )
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
                            fontSize = 14.sp.from(ctx),
                            color = Secondary,
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
                            .background(Primary)
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
                                    "${addressStateUI.addressFirst.uppercase(Locale.getDefault())},"
                                )
                            }
                            withStyle(
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 12.sp.from(ctx),
                                    color = Color.Gray,
                                ).toSpanStyle()
                            ) {
                                append(addressStateUI.addressSecond.uppercase(Locale.getDefault()))
                            }
                        })
                }
            }
            item {
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 12.dp.from(ctx),
                                vertical = 10.dp.from(ctx)
                            ),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Where to?",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp.from(ctx),
                                color = Color.Black,
                            ),
                        )
                        Text(
                            text = "Manage",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp.from(ctx),
                                color = Secondary,
                            )
                        )
                    }
                    LazyRow(content = {
                        items(3) {
                            Card(
                                modifier = modifier
                                    .height(150.dp.from(ctx))
                                    .width(150.dp.from(ctx))
                                    .padding(
                                        start = when (it) {
                                            0 -> 12.dp.from(ctx)
                                            else -> 10.dp
                                        }
                                    ),
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 1.dp.from(
                                        ctx
                                    )
                                ),
                                shape = RoundedCornerShape(12.dp.from(ctx)),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                border = BorderStroke(0.8.dp.from(ctx), color = GrayBG)
                            ) {

                            }
                        }
                    })
                }
            }
        }
    })
}

@Composable
@Preview(showSystemUi = true)
fun DisplayDashboard() {
    LiveTrackingTheme {
        PageDashboard()
    }
}