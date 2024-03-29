package com.example.livetracking.ui.page.direction

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.LaunchedEffect
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.livetracking.domain.model.GyroData
import com.example.livetracking.service.LocationService
import com.example.livetracking.ui.component.bottomsheet.rememberBottomSheetScaffoldState
import com.example.livetracking.ui.page.direction.Direction.onBack
import com.example.livetracking.ui.page.direction.Direction.placeIdArgs
import com.example.livetracking.utils.toast
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

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.routeDirection(
    navHostController: NavHostController
) {
    composable(
        "${Direction.routeName}/{$placeIdArgs}", arguments = listOf(
            navArgument(placeIdArgs) { type = NavType.StringType }
        )
    ) {
        val context = LocalContext.current
        val focusRequester = remember { FocusRequester() }
        val interactionSource = MutableInteractionSource()
        val viewModel = hiltViewModel<ViewModelDirection>()
        var textSearch by remember { mutableStateOf("") }
        val scope = rememberCoroutineScope()
        val sheetState = rememberBottomSheetScaffoldState()
        var isDirectionState by remember { mutableStateOf(false) }

        val locationStateUI by viewModel.locationStateUI.collectAsStateWithLifecycle(initialValue = LocationStateUI())
        val destinationStateUI by viewModel.destinationStateUI.observeAsState(initial = DestinationStateUI())
        val directionStateUI by viewModel.directionStateUI.observeAsState(initial = DirectionStateUI())
        val gyroScopeStateUI by viewModel.gyroScopeStateUI.observeAsState(initial = GyroData())
        val isShareState by LocationService.IS_SHARING.collectAsStateWithLifecycle(initialValue = false)
        val urlSharing by viewModel.urlSharing.collectAsStateWithLifecycle(initialValue = SharingURLState())

        val bounds = locationStateUI.myLoc?.let { it1 ->
            destinationStateUI.destination?.let { it2 ->
                LatLngBounds.builder()
                    .include(
                        it1
                    )
                    .include(
                        it2
                    ).build()
            }
        }


        val cameraPositionState = rememberCameraPositionState {
            position = locationStateUI.myLoc?.let { it1 ->
                CameraPosition.fromLatLngZoom(
                    it1, 15f
                )
            } ?: kotlin.run {
                CameraPosition.fromLatLngZoom(
                    LatLng(0.0, 0.0), 0f
                )
            }
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

        fun updateUiAndLocation() = scope.launch {
            bounds?.let { it1 ->
                CameraUpdateFactory.newLatLngBounds(
                    it1,
                    120
                )
            }?.let { it2 ->
                cameraPositionState.animate(
                    update = it2,
                    durationMs = 1000
                )
            }
        }

        fun updateDirectionCamera() = scope.launch {
            locationStateUI.myLoc?.let {
                cameraPositionState.animate(
                    update = CameraUpdateFactory.newCameraPosition(
                        CameraPosition(
                            it, 25f, 50f, gyroScopeStateUI.azimuth
                        ),
                    ),
                    durationMs = 100
                )
            }
        }

        fun onShareLocation() {
            if (!isShareState)
                viewModel.sendDestinationAndPolyline()
            else
                viewModel.stopLocationService()

        }

        fun copyUrl() {
            val clipboardManager =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboardManager.setPrimaryClip(
                ClipData.newPlainText(
                    "url_sharing_location",
                    urlSharing.url
                )
            )
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) context.toast("Copied")
        }

        LaunchedEffect(key1 = locationStateUI, key2 = gyroScopeStateUI.azimuth, block = {
            if (isDirectionState) {
                updateDirectionCamera()
            }
        })

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
            myLoc = locationStateUI.myLoc,
            onBackStack = {
                with(navHostController) {
                    onBack()
                }
            },
            mapsUiSettings = mapsUiSettings,
            onMyLocationButtonClick = {

            },
            onMapLoaded = {
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
            estimateDistanceAndTime = directionStateUI.data.lastOrNull()?.estimate ?: "",
            destinationImage = destinationStateUI.image,
            destinationLoading = destinationStateUI.loading,
            directionLoading = directionStateUI.loading,
            isDirection = isDirectionState,
            isShare = isShareState,
            urlSharing = urlSharing.url,
            onDirectionClick = {
                isDirectionState = !isDirectionState
            },
            onShareLocation = ::onShareLocation,
            copyUrl = ::copyUrl
        )
    }
}

data class SharingURLState(
    val url: String = "",
    val id: String = "",
)