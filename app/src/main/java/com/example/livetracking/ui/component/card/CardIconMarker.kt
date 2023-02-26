package com.example.livetracking.ui.component.card

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.livetracking.R
import com.example.livetracking.ui.theme.Primary
import com.example.livetracking.utils.from
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.fade
import com.google.accompanist.placeholder.placeholder

@Composable
fun CardIconMarker(
    modifier: Modifier = Modifier,
    ctx: Context,
    isLoading: Boolean = false
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(Primary)
            .placeholder(
                visible = isLoading,
                highlight = PlaceholderHighlight.fade(),
                shape = CircleShape,
                color = Color.Gray,
            ),
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_location_on_24),
            contentDescription = "ic_marker",
            tint = Color.White,
            modifier = Modifier.padding(5.dp.from(ctx))
        )
    }
}