package com.example.livetracking.ui.component.bottomsheet

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Build
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.livetracking.utils.coloredShadow
import com.example.livetracking.utils.from

@Composable
fun BottomSheetDirection(
    modifier: Modifier = Modifier,
    context: Context,
) {
    LazyColumn(content = {
        item {
            Row(modifier = modifier.fillMaxWidth()) {
                Icon(Icons.Sharp.Build, contentDescription = "")
                Text(text = "TEST BOTTOM SHEET")
            }
        }
        items(10) {
            Text(text = "TEST = $it")
        }
    })
}