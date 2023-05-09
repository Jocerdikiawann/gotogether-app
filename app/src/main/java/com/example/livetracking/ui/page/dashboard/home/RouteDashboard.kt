package com.example.livetracking.ui.page.dashboard.home

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.livetracking.domain.model.GyroData
import com.example.livetracking.domain.model.LocationData
import com.example.livetracking.ui.page.dashboard.main.DashboardMain
import com.example.livetracking.utils.PermissionUtils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

object Dashboard {
    const val routeName: String = "dashboard"
}

fun NavGraphBuilder.routeDashboard(
    onNavigateToItemDashboard: (String) -> Unit,
    onNavigateToSearchLocation: (LocationData) -> Unit,
) {
    composable(Dashboard.routeName) {
        val ctx = LocalContext.current
        val viewModel = hiltViewModel<ViewModelDashboard>()
        val scope = rememberCoroutineScope()

        val havePermission by viewModel.havePermission.observeAsState(LocationStateUI())
        val dashboardStateUI by viewModel.dashboardStateUI.observeAsState(DashboardStateUI())
        val addressStateUI by viewModel.addressStateUI.observeAsState(AddressStateUI())
        val gyroScopeStateUI by viewModel.gyroScopeStateUI.observeAsState(GyroData())

        var mapsReady by remember { mutableStateOf(false) }

        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition(
                LatLng(
                    dashboardStateUI.lat,
                    dashboardStateUI.lng
                ), 15f, 0f, 0f
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

        val resultLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartIntentSenderForResult(),
            onResult = { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    viewModel.startLocationUpdate()
                }
            })

        val permissionUtils = PermissionUtils(ctx)
        val permissionLauncher =
            rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestMultiplePermissions()) {
                viewModel.havePermission()
            }

        val interactionSource = MutableInteractionSource()
        val focusRequester = remember { FocusRequester() }

        fun updateUiAndLocation() {
            scope.launch {
                cameraPositionState.animate(
                    update = CameraUpdateFactory.newCameraPosition(
                        CameraPosition(
                            LatLng(
                                dashboardStateUI.lat,
                                dashboardStateUI.lng
                            ), 15f, 0f, 0f
                        ),
                    ),
                    durationMs = 1000
                )
            }
        }

        DisposableEffect(key1 = havePermission, effect = {
            if (!havePermission.isGpsOn && havePermission.permission == true) {
                viewModel.turnOnGps(resultLauncher)
            }
            onDispose { }
        })

        DisposableEffect(dashboardStateUI) {
            if (mapsReady) {
                updateUiAndLocation()
            }
            onDispose { }
        }

        DashboardMain(
            currentRoute = it.destination.route ?: "",
            onItemClick = { route ->
                onNavigateToItemDashboard(route)
            }) {
            PageDashboard(
                onGivePermission = {
                    permissionLauncher.launch(permissionUtils.listPermission())
                },
                havePermission = havePermission,
                dashboardStateUI = dashboardStateUI,
                mapsUiSettings = mapsUiSettings,
                addressStateUI = addressStateUI,
                cameraPositionState = cameraPositionState,
                ctx = ctx,
                onMapReady = { isReady ->
                    mapsReady = isReady
                    updateUiAndLocation()
                },
                onClickSearchField = {
                    onNavigateToSearchLocation(
                        LocationData(dashboardStateUI.lat, dashboardStateUI.lng)
                    )
                },
                focusRequester = focusRequester,
                interactionSource = interactionSource,
                rotationMarker = gyroScopeStateUI.azimuth
            )
        }
    }
}