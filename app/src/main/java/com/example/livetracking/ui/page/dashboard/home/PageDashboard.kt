package com.example.livetracking.ui.page.dashboard.home

import android.content.Context
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.livetracking.ui.component.card.CardIconMarker
import com.example.livetracking.ui.component.card.CardMap
import com.example.livetracking.ui.component.card.CardNotPermission
import com.example.livetracking.ui.component.card.CardRowSavedLocation
import com.example.livetracking.ui.component.textfield.TextFieldSearch
import com.example.livetracking.ui.theme.LiveTrackingTheme
import com.example.livetracking.ui.theme.Secondary
import com.example.livetracking.utils.from
import com.example.livetracking.utils.toTitleCase
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.fade
import com.google.accompanist.placeholder.placeholder
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.MapUiSettings
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
    val permission: Boolean? = null,
    val isGpsOn: Boolean = false,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageDashboard(
    modifier: Modifier = Modifier,
    havePermission: LocationStateUI,
    dashboardStateUI: DashboardStateUI,
    cameraPositionState: CameraPositionState,
    onMapReady: (Boolean) -> Unit,
    mapsUiSettings: MapUiSettings,
    updateUiAndLocation: () -> Unit,
    addressStateUI: AddressStateUI,
    onGivePermission: () -> Unit,
    onClickSearchField: () -> Unit,
    focusRequester: FocusRequester,
    interactionSource: MutableInteractionSource,
    ctx: Context,
) {
    when (havePermission.permission) {
        null -> Unit
        true -> {
            LazyColumn(modifier = modifier
                .fillMaxSize()
                .padding(top = 15.dp.from(ctx)), content = {
                item {
                    TextFieldSearch(
                        onValueChange = {},
                        enable = false,
                        readOnly = true,
                        placeHolder = "Find your destination...",
                        value = "",
                        context = ctx,
                        onClickSearchField = { onClickSearchField() },
                        focusRequester = focusRequester,
                        interactionSource = interactionSource,
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
                            onMapReady(true)
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
                                    15f
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
                        CardIconMarker(
                            ctx = ctx,
                            modifier = modifier
                                .width(30.dp.from(ctx))
                                .height(30.dp.from(ctx))
                        )
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
                                        "${
                                            addressStateUI.addressFirst.toTitleCase()
                                        },"
                                    )
                                }
                                withStyle(
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 12.sp.from(ctx),
                                        color = Color.Gray,
                                    ).toSpanStyle()
                                ) {
                                    append(addressStateUI.addressSecond.toTitleCase())
                                }
                            })
                    }
                }
                item {
                    Column(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(vertical = 15.dp.from(ctx))
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
                                CardRowSavedLocation(index = it, ctx = ctx)
                            }
                        })
                    }
                }
            })
        }
        false -> {
            CardNotPermission(ctx = ctx) {
                onGivePermission()
            }
        }
    }

}

@Composable
@Preview(showSystemUi = true)
fun DisplayDashboard() {
    LiveTrackingTheme {
    }
}