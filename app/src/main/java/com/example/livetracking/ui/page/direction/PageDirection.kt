package com.example.livetracking.ui.page.direction

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.location.Location
import android.os.Bundle
import android.view.animation.LinearInterpolator
import androidx.compose.animation.ExperimentalAnimationApi
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.livetracking.R
import com.example.livetracking.ui.component.bottomsheet.BottomSheetDirection
import com.example.livetracking.ui.component.bottomsheet.BottomSheetScaffold
import com.example.livetracking.ui.component.bottomsheet.BottomSheetScaffoldState
import com.example.livetracking.ui.component.textfield.TextFieldSearch
import com.example.livetracking.utils.BitmapDescriptor
import com.example.livetracking.utils.from
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.JointType.ROUND
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.SquareCap
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi

data class DestinationStateUI(
    val loading: Boolean = false,
    val error: Boolean = false,
    val errMsg: String = "",
    val title: String = "",
    val address: String = "",
    val image: Bitmap? = null,
    val destination: LatLng? = null,
)

data class DirectionStateUI(
    val loading: Boolean = false,
    val error: Boolean = false,
    val errMsg: String = "",
    val data: List<DirectionData> = listOf()
)

data class DirectionData(
    val estimate: String = "",
    val route: List<LatLng> = listOf()
)

data class LocationStateUI(
    val myLoc: LatLng? = null,
)

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class,
    MapsComposeExperimentalApi::class
)
@Composable
fun PageDirection(
    modifier: Modifier = Modifier,
    context: Context,
    textSearch: String,
    focusRequester: FocusRequester,
    interactionSource: MutableInteractionSource,
    onValueChange: (String) -> Unit,
    onBackStack: () -> Unit,
    myLoc: LatLng?,
    destination: LatLng?,
    route: List<LatLng>,
    title: String,
    address: String,
    estimateDistanceAndTime: String,
    destinationImage: Bitmap?,
    destinationLoading: Boolean,
    directionLoading: Boolean,
    isDirection: Boolean,
    onDirectionClick: () -> Unit,
    cameraPositionState: CameraPositionState,
    googleMapOptions: () -> GoogleMapOptions,
    mapsUiSettings: MapUiSettings,
    rotationMarker: Float,
    onMapLoaded: () -> Unit,
    sheetState: BottomSheetScaffoldState,
    onMyLocationButtonClick: (Location) -> Unit
) {
    var marker by remember { mutableStateOf<Marker?>(value = null) }

    BottomSheetScaffold(
        sheetContent = {
            BottomSheetDirection(
                context = context,
                estimateDistanceAndTime = estimateDistanceAndTime,
                address = address,
                title = title,
                imageUrl = destinationImage,
                directionLoading = directionLoading,
                destinationLoading = destinationLoading,
                isDirection = isDirection,
                onDirectionClick = onDirectionClick,
            )
        },
        scaffoldState = sheetState,
        sheetPeekHeight = 150.dp.from(context),
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
                googleMapOptionsFactory = {
                    googleMapOptions()
                },
                onMapLoaded = { onMapLoaded() },
                uiSettings = mapsUiSettings,
                onMyLocationClick = {
                    onMyLocationButtonClick(it)
                },
            ) {
                MapEffect(key1 = route.isNotEmpty()) { map ->
                    myLoc?.let {
                        marker = map.addMarker(
                            MarkerOptions().position(myLoc).title("Your Loc").icon(
                                BitmapDescriptor(context, R.drawable.ic_directions)
                            )
                        )
                    }
                    destination?.let {
                        map.addMarker(
                            MarkerOptions().position(destination).title("Your Destination")
                        )
                    }
                    val grayPolyline = map.addPolyline(
                        PolylineOptions()
                            .color(android.graphics.Color.GRAY)
                            .startCap(SquareCap())
                            .endCap(SquareCap())
                            .jointType(ROUND)
                            .addAll(route)
                    )

                    val primaryPolyLine = map.addPolyline(
                        PolylineOptions()
                            .color(R.color.primary)
                            .startCap(SquareCap())
                            .endCap(SquareCap())
                            .jointType(ROUND)
                    )

                    val polylineAnimator = ValueAnimator.ofInt(0, 100)
                    polylineAnimator.duration = 2000
                    polylineAnimator.interpolator = LinearInterpolator()
                    polylineAnimator.addUpdateListener {
                        val points = grayPolyline.points
                        val percentValue = it.animatedValue as Int
                        val size = points.size
                        val newPoints = (size * (percentValue / 100.0f)).toInt()
                        val p = points.subList(0, newPoints)
                        primaryPolyLine.points = p
                    }
                    polylineAnimator.start()
                }
                MapEffect(key1 = myLoc) {
                    myLoc?.let {
                        val valueAnimator = ValueAnimator.ofFloat(0F, 1F)
                        valueAnimator.duration = 3000
                        valueAnimator.interpolator = LinearInterpolator()
                        valueAnimator.addUpdateListener { anime ->
                            val v = anime.animatedFraction
                            val prevPosition = marker?.position
                            prevPosition?.let {
                                val newLng = (1 - v) * it.longitude + v * myLoc.longitude
                                val newLat = (1 - v) * it.latitude + v * myLoc.latitude
                                marker?.position = LatLng(newLat, newLng)
                                marker?.setAnchor(0.5f, 0.5f)
                            }
                        }
                        valueAnimator.start()
                    }
                }
                MapEffect(key1 = rotationMarker, block = {
                    marker?.rotation = rotationMarker
                })
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

@Composable
fun rememberMapViewLifecycle(): MapView {
    val context = LocalContext.current

    val mapView = remember {
        MapView(context).apply {
            id = R.id.map
        }
    }

    val lifeCycleObserver = rememberMapLifecycleObserver(mapView)
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    DisposableEffect(lifecycle) {
        lifecycle.addObserver(lifeCycleObserver)
        onDispose { lifecycle.removeObserver(lifeCycleObserver) }
    }

    return mapView
}

@Composable
fun rememberMapLifecycleObserver(mapView: MapView): LifecycleEventObserver = remember(mapView) {
    LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> mapView.onCreate(Bundle())
            Lifecycle.Event.ON_START -> mapView.onStart()
            Lifecycle.Event.ON_RESUME -> mapView.onResume()
            Lifecycle.Event.ON_PAUSE -> mapView.onPause()
            Lifecycle.Event.ON_STOP -> mapView.onStop()
            Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
            else -> throw IllegalStateException()
        }
    }
}