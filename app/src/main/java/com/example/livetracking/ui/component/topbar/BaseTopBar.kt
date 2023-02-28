package com.example.livetracking.ui.component.topbar

import android.content.Context
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.livetracking.utils.from

@Composable
fun BaseTopBar(
    modifier: Modifier = Modifier,
    context: Context,
    title: String,
    onBackPressed: () -> Unit
) {
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = { onBackPressed() }) {
            Icon(
                Icons.Sharp.ArrowBack,
                contentDescription = "ic_left_arrow",
                modifier = modifier
                    .height(30.dp.from(context))
                    .width(30.dp.from(context))
            )
        }
        Spacer(modifier = modifier.width(20.dp.from(context)))
        Text(
            text = title, style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 16.sp.from(context),
                fontWeight = FontWeight.Medium
            )
        )
    }
}