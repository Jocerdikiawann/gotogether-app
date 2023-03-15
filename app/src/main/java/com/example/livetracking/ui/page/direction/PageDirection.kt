package com.example.livetracking.ui.page.direction

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.location.Location
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.livetracking.R
import com.example.livetracking.ui.component.bottomsheet.BottomSheetDirection
import com.example.livetracking.ui.component.bottomsheet.BottomSheetScaffold
import com.example.livetracking.ui.component.bottomsheet.BottomSheetScaffoldState
import com.example.livetracking.ui.component.textfield.TextFieldSearch
import com.example.livetracking.utils.BitmapDescriptor
import com.example.livetracking.utils.from
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline

data class DestinationStateUI(
    val loading: Boolean = true,
    val error: Boolean = true,
    val errMsg: String = "",
    val destination: LatLng = LatLng(-6.2881792, 106.7614208),
)

data class DirectionStateUI(
    val loading: Boolean = true,
    val error: Boolean = true,
    val errMsg: String = "",
    val data: List<DirectionData> = listOf()
)

data class DirectionData(
    val duration: String = "",
    val distance: String = "",
    val route: List<LatLng> = listOf()
)

data class LocationStateUI(
    val lat: Double = -6.2881792,
    val lng: Double = 106.7614208,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageDirection(
    modifier: Modifier = Modifier,
    context: Context,
    textSearch: String,
    focusRequester: FocusRequester,
    interactionSource: MutableInteractionSource,
    onValueChange: (String) -> Unit,
    onBackStack: () -> Unit,
    myLoc: LatLng,
    destination: LatLng,
    route: List<LatLng>,
    cameraPositionState: CameraPositionState,
    googleMapOptions: () -> GoogleMapOptions,
    mapsUiSettings: MapUiSettings,
    rotationMarker: Float,
    onMapLoaded: () -> Unit,
    sheetState: BottomSheetScaffoldState,
    onMyLocationButtonClick: (Location) -> Unit
) {
    BottomSheetScaffold(
        sheetContent = {
            BottomSheetDirection(context = context)
        },
        scaffoldState = sheetState,
        sheetPeekHeight = 120.dp.from(context),
        backgroundColor = Color.Transparent,
        sheetShape = RoundedCornerShape(
            topStart = 20.dp.from(context),
            topEnd = 20.dp.from(context)
        ),
        sheetBackgroundColor = Color.White,
        sheetElevation = 20.dp.from(context)
    ) {
        BoxWithConstraints(modifier = modifier.fillMaxSize()) {
            GoogleMap(
                modifier = modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                cameraPositionState = cameraPositionState,
                googleMapOptionsFactory = { googleMapOptions() },
                onMapLoaded = { onMapLoaded() },
                uiSettings = mapsUiSettings,
                onMyLocationClick = {
                    onMyLocationButtonClick(it)
                }
            ) {
                Marker(
                    state = MarkerState(destination),
                    title = "Destination Location Here",
                    snippet = "marker in destination location",
                )
                Marker(
                    icon = BitmapDescriptor(context, R.drawable.ic_directions),
                    state = MarkerState(myLoc),
                    title = "Your Location Here",
                    snippet = "marker in your location",
                    rotation = rotationMarker
                )
                Polyline(points = route)
            }
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(vertical = 10.dp.from(context))
            ) {
                TextFieldSearch(onValueChange = {
                    onValueChange(it)
                },
                    enable = true,
                    readOnly = false,
                    placeHolder = "",
                    value = textSearch,
                    context = context,
                    focusRequester = focusRequester,
                    interactionSource = interactionSource,
                    leadingIcon = {
                        IconButton(onClick = { onBackStack() }) {
                            Icon(
                                Icons.Sharp.ArrowBack,
                                contentDescription = "ic_left_arrow",
                                modifier = modifier
                                    .height(30.dp.from(context))
                                    .width(30.dp.from(context))
                            )
                        }
                    })
            }
        }
    }
}