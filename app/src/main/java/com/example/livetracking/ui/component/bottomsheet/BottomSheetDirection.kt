package com.example.livetracking.ui.component.bottomsheet

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.livetracking.R
import com.example.livetracking.ui.theme.Primary
import com.example.livetracking.ui.theme.Secondary
import com.example.livetracking.utils.from
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.fade
import com.google.accompanist.placeholder.placeholder

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BottomSheetDirection(
    modifier: Modifier = Modifier,
    destinationLoading: Boolean,
    directionLoading: Boolean,
    imageUrl: Bitmap?,
    title: String,
    address: String,
    estimateDistanceAndTime: String,
    context: Context,
    isDirection: Boolean,
    onDirectionClick: () -> Unit
) {
    LazyColumn(
        content = {
            item {
                Row(
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = modifier
                            .height(5.dp.from(context))
                            .width(50.dp.from(context))
                            .clip(RoundedCornerShape(10.dp.from(context)))
                            .background(
                                Color.Gray.copy(
                                    alpha = .9f
                                )
                            ),
                    )
                }
            }
            item {
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(12.dp.from(context)),
                ) {
                    Image(
                        bitmap = imageUrl?.asImageBitmap()
                            ?: ImageBitmap.imageResource(id = R.drawable.placeholder_image_default),
                        contentDescription = "image_place",
                        contentScale = ContentScale.Crop,
                        modifier = modifier
                            .height(100.dp.from(context))
                            .width(100.dp.from(context))
                            .clip(RoundedCornerShape(8.dp.from(context)))
                            .border(
                                BorderStroke(1.dp.from(context), color = Color.Gray),
                                shape = RoundedCornerShape(8.dp.from(context))
                            )
                            .placeholder(
                                visible = destinationLoading,
                                highlight = PlaceholderHighlight.fade(),
                                shape = RoundedCornerShape(8.dp.from(context)),
                                color = Color.Gray
                            ),
                    )
                    Spacer(modifier = modifier.width(10.dp.from(context)))
                    Column(horizontalAlignment = Alignment.Start) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 14.sp.from(context),
                                fontWeight = FontWeight.Medium
                            ),
                            modifier = modifier
                                .fillMaxWidth()
                                .placeholder(
                                    visible = destinationLoading,
                                    highlight = PlaceholderHighlight.fade(),
                                    shape = RoundedCornerShape(8.dp.from(context)),
                                    color = Color.Gray,
                                )
                        )
                        Spacer(modifier.height(5.dp.from(context)))
                        Text(
                            text = address,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 12.sp.from(context),
                                fontWeight = FontWeight.Normal,
                                color = Color.Gray
                            ),
                            modifier = modifier
                                .fillMaxWidth()
                                .placeholder(
                                    visible = destinationLoading,
                                    highlight = PlaceholderHighlight.fade(),
                                    shape = RoundedCornerShape(8.dp.from(context)),
                                    color = Color.Gray,
                                ),
                        )
                        Spacer(modifier.height(10.dp.from(context)))
                        Text(
                            text = estimateDistanceAndTime,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 12.sp.from(context),
                                fontWeight = FontWeight.Normal,
                                color = Color.Gray
                            ),
                            modifier = modifier
                                .fillMaxWidth()
                                .placeholder(
                                    visible = directionLoading,
                                    highlight = PlaceholderHighlight.fade(),
                                    shape = RoundedCornerShape(8.dp.from(context)),
                                    color = Color.Gray,
                                )
                        )
                        Spacer(modifier.height(5.dp.from(context)))
                        Row(
                            modifier = modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            OutlinedButton(
                                onClick = onDirectionClick,
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = Color.White,
                                    contentColor = if (isDirection) Secondary else Primary
                                ),
                                border = BorderStroke(1.dp, Color.Gray),
                                shape = RoundedCornerShape(8.dp.from(context))
                            ) {
                                Text(
                                    text = if (isDirection) "Stop" else "Direction",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 12.sp.from(context),
                                        fontWeight = FontWeight.Normal,
                                    )
                                )
                            }
                            Spacer(modifier = modifier.width(10.dp.from(context)))
                            OutlinedButton(
                                onClick = { },
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = Color.White,
                                ),
                                border = BorderStroke(1.dp, Color.Gray),
                                shape = RoundedCornerShape(8.dp.from(context)),
                                enabled = isDirection
                            ) {
                                Text(
                                    text = "Share",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 12.sp.from(context),
                                        fontWeight = FontWeight.Normal,
                                    )
                                )
                            }
                        }
                    }
                }
            }
        })
}

@Composable
@Preview
fun PreviewBottomSheetDirection() {
    BottomSheetDirection(
        destinationLoading = false,
        directionLoading = false,
        imageUrl = null,
        title = "Tes",
        address = "Tes Address",
        estimateDistanceAndTime = "2KM",
        context = LocalContext.current,
        isDirection = false
    ) {

    }
}