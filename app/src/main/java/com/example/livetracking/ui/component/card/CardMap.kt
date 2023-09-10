package com.example.livetracking.ui.component.card

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.livetracking.R
import com.example.livetracking.utils.BitmapDescriptor
import com.example.livetracking.utils.from
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState

@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun CardMap(
    modifier: Modifier = Modifier,
    ctx: Context,
    latLng: LatLng,
    rotationMarker: Float,
    cameraPositionState: CameraPositionState,
    googleMapOptions: () -> GoogleMapOptions,
    mapsUiSettings: MapUiSettings
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp.from(ctx))
            .padding(horizontal = 12.dp.from(ctx)),
        elevation = CardDefaults.cardElevation(),
        shape = RoundedCornerShape(10.dp.from(ctx)),
        border = BorderStroke(0.8.dp, Color.Gray)
    ) {
        GoogleMap(
            modifier = modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            googleMapOptionsFactory = { googleMapOptions() },
            uiSettings = mapsUiSettings,
        ) {
            Marker(
                icon = BitmapDescriptor(ctx, R.drawable.ic_directions),
                state = MarkerState(latLng),
                title = "Your Location Here",
                snippet = "marker in your location",
                rotation = rotationMarker,
            )
        }
    }
}