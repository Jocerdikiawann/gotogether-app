package com.example.livetracking.ui.component.card

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.livetracking.R
import com.example.livetracking.ui.theme.GrayBG
import com.example.livetracking.utils.from

@Composable
fun CardRecentHistory(
    modifier: Modifier = Modifier,
    context: Context,
    onClickAction: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = 12.dp.from(context),
                vertical = 10.dp.from(context)
            ),
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = modifier
                .clip(CircleShape)
                .background(GrayBG)
                .width(40.dp.from(context))
                .height(40.dp.from(context)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_history),
                contentDescription = "ic_marker_history",
            )
        }
        Spacer(modifier = modifier.width(10.dp.from(context)))
        Column(horizontalAlignment = Alignment.Start) {
            Text(
                text = "Buka Botol Gading Serpong",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp.from(context)
                ),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
            Spacer(modifier = modifier.height(5.dp.from(context)))
            Text(
                text = "Pasar Modern Paramount Blok H No.10, Curug Sangereng, Kec. Klp. Dua, Kabupaten Tangerang, Banten 15810",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Light,
                    fontSize = 14.sp.from(context),
                    color = Color.Gray
                ),
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
            )
        }
    }
}