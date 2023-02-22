package com.example.livetracking.ui.component.topbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.livetracking.R
import com.example.livetracking.ui.theme.LiveTrackingTheme
import com.example.livetracking.utils.from


@Composable
fun ProfileBar(modifier: Modifier = Modifier,img:String="",name:String="Jon Doe") {
    val ctx = LocalContext.current
    Row(modifier = modifier.padding(12.dp.from(ctx))) {
        Column {
            Box(
                modifier = modifier
                    .width(50.dp.from(ctx))
                    .height(50.dp.from(ctx))
                    .clip(CircleShape)
            ) {
                Image(
                    painter = rememberImagePainter(data = img, builder = {
                        crossfade(true)
                        error(R.drawable.user)
                        placeholder(R.drawable.user)
                    }),
                    contentDescription = "profile_user",
                    contentScale = ContentScale.FillWidth,
                    modifier = modifier
                        .fillMaxSize()
                        .clip(
                            CircleShape
                        )
                )
            }
            Spacer(modifier = modifier.height(10.dp.from(ctx)))
            Text(
                text = "Hi, $name", style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Light,
                    fontSize = 12.sp.from(ctx),
                    color = Color.Gray
                )
            )
        }

    }
}

@Composable
@Preview(showSystemUi = true)
fun DisplayProfileBar() {
    LiveTrackingTheme {
        ProfileBar()
    }
}