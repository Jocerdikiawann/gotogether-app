package com.example.livetracking.ui.page.direction

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.livetracking.domain.model.GyroData
import com.example.livetracking.ui.component.bottomsheet.rememberBottomSheetScaffoldState
import com.example.livetracking.ui.page.direction.Direction.onBack
import com.example.livetracking.ui.page.direction.Direction.placeIdArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

object Direction {
    const val routeName = "direction"
    const val placeIdArgs = "placeId"

    fun NavHostController.onBack() {
        popBackStack()
    }

    internal class DirectionArgs(val placeId: String) {
        constructor(savedStateHandle: SavedStateHandle) :
                this(checkNotNull(savedStateHandle[placeIdArgs]) as String)
    }
}

fun NavGraphBuilder.routeDirection(
    navHostController: NavHostController
) {
    composable(
        "${Direction.routeName}/{$placeIdArgs}", arguments = listOf(
            navArgument(placeIdArgs) { type = NavType.StringType }
        )
    ) {
        val context = LocalContext.current
        val focusRequester = remember {
            FocusRequester()
        }
        val interactionSource = MutableInteractionSource()
        val viewModel = hiltViewModel<ViewModelDirection>()
        var textSearch by remember { mutableStateOf("") }
        var scope = rememberCoroutineScope()
        val sheetState = rememberBottomSheetScaffoldState()

        var mapsReady by remember { mutableStateOf(false) }
        val locationStateUI by viewModel.locationStateUI.observeAsState(initial = LocationStateUI())
        val destinationStateUI by viewModel.destinationStateUI.observeAsState(initial = DestinationStateUI())
        val directionStateUI by viewModel.directionStateUI.observeAsState(initial = DirectionStateUI())
        val gyroScopeStateUI by viewModel.gyroScopeStateUI.observeAsState(initial = GyroData())

        val bounds = LatLngBounds.builder()
            .include(
                LatLng(
                    locationStateUI.lat,
                    locationStateUI.lng
                )
            )
            .include(
                destinationStateUI.destination
            ).build()


        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(
                LatLng(
                    locationStateUI.lat,
                    locationStateUI.lng
                ), 15f
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

        fun updateUiAndLocation() {
            scope.launch {
                cameraPositionState.animate(
                    update = CameraUpdateFactory.newLatLngBounds(
                        bounds,
                        100
                    ),
                    durationMs = 1000
                )
            }
        }

        PageDirection(
            context = context,
            sheetState = sheetState,
            textSearch = textSearch,
            focusRequester = focusRequester,
            interactionSource = interactionSource,
            onValueChange = {
                textSearch = it
            },
            cameraPositionState = cameraPositionState,
            myLoc = LatLng(locationStateUI.lat, locationStateUI.lng),
            onBackStack = {
                with(navHostController) {
                    onBack()
                }
            },
            mapsUiSettings = mapsUiSettings,
            onMyLocationButtonClick = {

            },
            onMapLoaded = {
                mapsReady = true
                updateUiAndLocation()
            },
            googleMapOptions = {
                GoogleMapOptions()
            },
            destination = destinationStateUI.destination,
            route = directionStateUI.data.firstOrNull()?.route ?: listOf(),
            rotationMarker = gyroScopeStateUI.azimuth,
            title = destinationStateUI.title,
            address = destinationStateUI.address,
            estimateDistance = directionStateUI.data.lastOrNull()?.distance ?: "",
            estimateTime = directionStateUI.data.lastOrNull()?.duration ?: "",
            destinationImage = destinationStateUI.image,
            destinationLoading = destinationStateUI.loading,
            directionLoading = directionStateUI.loading
        )
    }
}