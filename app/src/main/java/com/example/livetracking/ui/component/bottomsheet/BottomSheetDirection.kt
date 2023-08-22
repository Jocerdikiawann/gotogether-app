package com.example.livetracking.ui.component.bottomsheet

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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

@Composable
fun BottomSheetDirection(
    destinationLoading: Boolean,
    directionLoading: Boolean,
    imageUrl: Bitmap?,
    title: String,
    address: String,
    estimateDistanceAndTime: String,
    context: Context,
    isDirection: Boolean,
    isShare: Boolean,
    url:String,
    copyUrl:()->Unit,
    onDirectionClick: () -> Unit,
    onShareLocation: () -> Unit,
) {
    LazyColumn(
        content = {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp.from(context)),
                ) {
                    Image(
                        bitmap = imageUrl?.asImageBitmap()
                            ?: ImageBitmap.imageResource(id = R.drawable.placeholder_image_default),
                        contentDescription = "image_place",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .wrapContentHeight()
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
                    Spacer(modifier = Modifier.width(10.dp.from(context)))
                    Column(horizontalAlignment = Alignment.Start) {
                        if(isShare){
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { copyUrl() }
                                    .clip(RoundedCornerShape(8.dp.from(context)))
                                    .background(Color.Gray.copy(alpha = .2f)),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = url,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 14.sp.from(context),
                                        color = Color.Gray
                                    ),
                                    modifier = Modifier
                                        .weight(2f)
                                        .padding(
                                            start = 10.dp.from(context),
                                            top = 10.dp.from(context),
                                            bottom = 10.dp.from(context),
                                        ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_copy_content),
                                    contentDescription = "ic_copy",
                                    tint = Color.Black,
                                    modifier = Modifier
                                        .weight(0.3f)
                                        .padding(horizontal=10.dp)
                                )
                            }
                            Spacer(Modifier.height(5.dp.from(context)))
                        }
                        Text(
                            text = title,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 14.sp.from(context),
                                fontWeight = FontWeight.Medium
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .placeholder(
                                    visible = destinationLoading,
                                    highlight = PlaceholderHighlight.fade(),
                                    shape = RoundedCornerShape(8.dp.from(context)),
                                    color = Color.Gray,
                                ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(Modifier.height(5.dp.from(context)))
                        Text(
                            text = address,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 12.sp.from(context),
                                fontWeight = FontWeight.Normal,
                                color = Color.Gray
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .placeholder(
                                    visible = destinationLoading,
                                    highlight = PlaceholderHighlight.fade(),
                                    shape = RoundedCornerShape(8.dp.from(context)),
                                    color = Color.Gray,
                                ),
                        )
                        Spacer(Modifier.height(10.dp.from(context)))
                        Text(
                            text = estimateDistanceAndTime,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 12.sp.from(context),
                                fontWeight = FontWeight.Normal,
                                color = Color.Gray
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .placeholder(
                                    visible = directionLoading,
                                    highlight = PlaceholderHighlight.fade(),
                                    shape = RoundedCornerShape(8.dp.from(context)),
                                    color = Color.Gray,
                                )
                        )
                        Spacer(Modifier.height(5.dp.from(context)))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
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
                                shape = RoundedCornerShape(8.dp.from(context)),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = if (isDirection) "Stop" else "Direction",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 12.sp.from(context),
                                        fontWeight = FontWeight.Normal,
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp.from(context)))
                            OutlinedButton(
                                onClick = onShareLocation,
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = Color.White,
                                ),
                                border = BorderStroke(1.dp, Color.Gray),
                                shape = RoundedCornerShape(8.dp.from(context)),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = if (isShare) "Stop" else "Share",
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
        isDirection = false,
        onDirectionClick = {},
        isShare = false,
        url="http://localhost:3000/jadkajsdkabsdkja",
        copyUrl = {}
    ) {

    }
}