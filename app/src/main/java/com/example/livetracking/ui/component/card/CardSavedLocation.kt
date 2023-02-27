package com.example.livetracking.ui.component.card

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.livetracking.R
import com.example.livetracking.ui.theme.GrayBG
import com.example.livetracking.ui.theme.LiveTrackingTheme
import com.example.livetracking.utils.from

@Composable
fun CardRowSavedLocation(
    modifier: Modifier = Modifier,
    index: Int,
    ctx: Context,
) {
    Card(
        modifier = modifier
            .height(150.dp.from(ctx))
            .width(250.dp.from(ctx))
            .padding(
                start = when (index) {
                    0 -> 12.dp.from(ctx)
                    else -> 10.dp
                }
            ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp.from(
                ctx
            )
        ),
        shape = RoundedCornerShape(12.dp.from(ctx)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp.from(ctx), color = Color.Gray)
    ) {
        Column(modifier = modifier.padding(12.dp.from(ctx))) {
            Column(
                modifier = modifier
                    .width(50.dp.from(ctx))
                    .height(50.dp.from(ctx))
                    .clip(CircleShape)
                    .background(GrayBG),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.house),
                    contentDescription = "ic_icon_$index",
                    contentScale = ContentScale.FillWidth,
                )
            }
            Spacer(modifier = modifier.height(12.dp.from(ctx)))
            Text(
                text = "Rumah pacar",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp.from(ctx)
                )
            )
            Spacer(modifier = modifier.height(8.dp.from(ctx)))
            Row(
                modifier = modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CardIconMarker(
                    ctx = ctx, modifier = modifier
                        .width(25.dp.from(ctx))
                        .height(25.dp.from(ctx))
                )
                Spacer(modifier = modifier.width(10.dp.from(ctx)))
                Text(
                    text = "Jl. H. Kuncin Rt005/003...",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp.from(ctx),
                        color = Color.Gray
                    )
                )
            }

        }
    }
}

@Composable
fun CardColumnSavedLocation(
    modifier: Modifier = Modifier,
    ctx: Context,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp.from(ctx), vertical = 10.dp.from(ctx))
            .clip(RoundedCornerShape(8.dp.from(ctx)))
            .border(
                BorderStroke(1.dp.from(ctx), color = Color.Gray),
                shape = RoundedCornerShape(8.dp.from(ctx))
            ),
    ) {
        Image(
            painter = rememberImagePainter(data = "", builder = {
                crossfade(true)
                error(R.drawable.placeholder_image_not_found)
                placeholder(R.drawable.placeholder_image_default)
            }),
            contentDescription = "img_",
            contentScale = ContentScale.FillWidth,
            modifier = modifier
                .fillMaxWidth()
                .height(150.dp.from(ctx))
                .padding(12.dp.from(ctx))
                .clip(RoundedCornerShape(8.dp.from(ctx)))
                .border(
                    BorderStroke(1.dp.from(ctx), color = Color.Gray),
                    shape = RoundedCornerShape(8.dp.from(ctx))
                )
        )
        Spacer(modifier = modifier.height(10.dp.from(ctx)))
        Text(
            text = "Rumah pacar", style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 14.sp.from(ctx),
                fontWeight = FontWeight.Bold
            ),
            modifier = modifier.padding(bottom = 8.dp.from(ctx), start = 12.dp.from(ctx))
        )
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp.from(ctx), start = 12.dp.from(ctx)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CardIconMarker(
                ctx = ctx, modifier = modifier
                    .width(25.dp.from(ctx))
                    .height(25.dp.from(ctx))
            )
            Spacer(modifier = modifier.width(10.dp.from(ctx)))
            Text(
                text = "Jl. H. Kuncin Rt005/003, Kec. pinang...",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp.from(ctx),
                    color = Color.Gray
                )
            )
        }

    }
}

@Composable
@Preview(showBackground = true)
fun DefaultPreview() {
    LiveTrackingTheme {
        CardColumnSavedLocation(ctx = LocalContext.current)
    }
}