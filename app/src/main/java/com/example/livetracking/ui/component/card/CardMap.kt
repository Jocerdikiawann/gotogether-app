package com.example.livetracking.ui.component.card

import android.content.Context
import android.location.Location
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.livetracking.ui.theme.GrayBG
import com.example.livetracking.utils.from
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun CardMap(
    modifier: Modifier = Modifier,
    ctx: Context,
    latLng: LatLng,
    cameraPositionState: CameraPositionState,
    googleMapOptions: () -> GoogleMapOptions,
    mapsUiSettings: MapUiSettings,
    onMapLoaded: () -> Unit,
    onMyLocationButtonClick: (Location)->Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp.from(ctx))
            .padding(horizontal = 12.dp.from(ctx)),
        elevation = CardDefaults.cardElevation(),
        shape = RoundedCornerShape(10.dp.from(ctx)),
        border = BorderStroke(0.8.dp, GrayBG)
    ) {
        Box(){
            GoogleMap(
                modifier = modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                googleMapOptionsFactory = { googleMapOptions() },
                onMapLoaded = { onMapLoaded() },
                uiSettings = mapsUiSettings,
                onMyLocationClick = {
                    onMyLocationButtonClick(it)
                }
            ) {
                Marker(
                    state = MarkerState(latLng),
                    title = "Your Location Here",
                    snippet = "marker in your location"
                )
            }
        }

    }
}