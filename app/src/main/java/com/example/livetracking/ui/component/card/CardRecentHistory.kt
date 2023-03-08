package com.example.livetracking.ui.component.card

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.fade
import com.google.accompanist.placeholder.placeholder

@Composable
fun CardRecentHistory(
    modifier: Modifier = Modifier,
    title: String,
    fullAddress: String,
    distance:String,
    context: Context,
    onClickAction: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onClickAction()
            }
            .padding(
                horizontal = 12.dp.from(context),
                vertical = 10.dp.from(context)
            ),
        horizontalArrangement = Arrangement.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
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
            Text(
                text = distance,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Light,
                    fontSize = 12.sp.from(context),
                    color = Color.Gray
                ),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
        }
        Spacer(modifier = modifier.width(10.dp.from(context)))
        Column(horizontalAlignment = Alignment.Start) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp.from(context)
                ),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
            Spacer(modifier = modifier.height(5.dp.from(context)))
            Text(
                text = fullAddress,
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

@Composable
fun CardRecentHistoryShimmer(
    modifier: Modifier = Modifier,
    context: Context,
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
                .height(40.dp.from(context))
                .placeholder(
                    visible = true,
                    highlight = PlaceholderHighlight.fade(),
                    shape = CircleShape,
                    color = Color.Gray
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_history),
                contentDescription = "ic_marker_history",
            )
        }
        Spacer(modifier = modifier.width(10.dp.from(context)))
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = modifier
                .fillMaxWidth()
                .placeholder(
                    visible = true,
                    highlight = PlaceholderHighlight.fade(),
                    shape = RoundedCornerShape(8.dp.from(context)),
                    color = Color.Gray
                )
        ) {
            Text(
                text = "",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp.from(context)
                ),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
            Spacer(modifier = modifier.height(5.dp.from(context)))
            Text(
                text = "",
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