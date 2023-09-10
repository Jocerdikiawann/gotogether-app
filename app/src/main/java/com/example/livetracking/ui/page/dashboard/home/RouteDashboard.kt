package com.example.livetracking.ui.page.dashboard.home

import android.app.Activity
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.livetracking.R
import com.example.livetracking.domain.model.GyroData
import com.example.livetracking.service.LocationService
import com.example.livetracking.ui.page.dashboard.main.DashboardMain
import com.example.livetracking.utils.BitmapDescriptor
import com.example.livetracking.utils.PermissionUtils
import com.example.livetracking.utils.from
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch
import timber.log.Timber

object Dashboard {
    const val routeName: String = "dashboard"
}

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.routeDashboard(
    onNavigateToItemDashboard: (String) -> Unit,
    onNavigateToSearchLocation: () -> Unit,
    onNavigateToDirection: (String) -> Unit,
) {
    composable(Dashboard.routeName) {
        val ctx = LocalContext.current
        val viewModel = hiltViewModel<ViewModelDashboard>()

        val havePermission by viewModel.havePermission.collectAsStateWithLifecycle(LocationStateUI())
        val dashboardStateUI by viewModel.dashboardStateUI.collectAsStateWithLifecycle(
            DashboardStateUI()
        )
        val addressStateUI by viewModel.addressStateUI.collectAsStateWithLifecycle(AddressStateUI())
        val gyroScopeStateUI by viewModel.gyroScopeStateUI.collectAsStateWithLifecycle(GyroData())
        val isSharing by LocationService.IS_SHARING.collectAsStateWithLifecycle(false)
        val placesId by viewModel.placesId.collectAsStateWithLifecycle("")
        val scope = rememberCoroutineScope()


        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(
                LatLng(
                    dashboardStateUI.lat,
                    dashboardStateUI.lng
                ), 15f
            )
        }

        val mapsUiSettings by remember {
            mutableStateOf(
                MapUiSettings(
                    compassEnabled = false,
                    zoomControlsEnabled = true,
                    myLocationButtonEnabled = true,
                )
            )
        }

        val resultLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartIntentSenderForResult(),
            onResult = { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    viewModel.getLocationUpdates()
                }
            })

        val permissionUtils = PermissionUtils(ctx)
        val permissionLauncher =
            rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestMultiplePermissions()) {
                viewModel.havePermission()
            }

        val interactionSource = MutableInteractionSource()
        val focusRequester = remember { FocusRequester() }

        suspend fun updateMarker(location: LatLng) {
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLng(location),
                durationMs = 1000
            )
        }

        LaunchedEffect(key1 = havePermission, block = {
            if (!havePermission.isGpsOn && havePermission.permission == true) viewModel.turnOnGps(
                resultLauncher
            )
        })

        LaunchedEffect(dashboardStateUI) {
            updateMarker(LatLng(dashboardStateUI.lat, dashboardStateUI.lng))
            Timber.tag("LOCATION").e(dashboardStateUI.toString())
        }

        LaunchedEffect(key1 = isSharing, block = {
            if (isSharing) {
                viewModel.getPlacesId()
            }
        })

        DashboardMain(
            currentRoute = it.destination.route ?: "",
            onItemClick = { route ->
                onNavigateToItemDashboard(route)
            },
            gotoDirection = {
                onNavigateToDirection(placesId)
            },
            isSharing = isSharing,
        ) {
            PageDashboard(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 15.dp.from(ctx)),
                havePermission = havePermission,
                dashboardStateUI = dashboardStateUI,
                cameraPositionState = cameraPositionState,
                rotationMarker = gyroScopeStateUI.azimuth,
                mapsUiSettings = mapsUiSettings,
                addressStateUI = addressStateUI,
                onGivePermission = {
                    permissionLauncher.launch(permissionUtils.listPermission())
                },
                onClickSearchField = onNavigateToSearchLocation,
                focusRequester = focusRequester,
                interactionSource = interactionSource,
                ctx = ctx,
                refresh= {
                    scope.launch {
                        updateMarker(LatLng(dashboardStateUI.lat, dashboardStateUI.lng))
                    }
                    viewModel.getAddress(dashboardStateUI.lat, dashboardStateUI.lng)
                }
            )
        }
    }
}