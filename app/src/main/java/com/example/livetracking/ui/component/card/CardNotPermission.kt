package com.example.livetracking.ui.component.card

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.livetracking.R
import com.example.livetracking.ui.theme.Primary
import com.example.livetracking.ui.theme.Secondary
import com.example.livetracking.utils.from

@Composable
fun CardNotPermission(
    modifier:Modifier=Modifier,
    ctx:Context,
    onClickButton:()->Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth().fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_permission_not_given),
            contentDescription = "ic_permission",
            modifier = modifier
                .width(250.dp.from(ctx))
                .height(250.dp.from(ctx)),
            tint = Primary
        )
        Text(
            text = "Please give permission and turn on GPS\nto access the application features.\uD83D\uDE15",
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp.from(ctx),
                color = Color.Black
            ),
            textAlign = TextAlign.Center
        )
        Button(onClick = {onClickButton()}, colors = ButtonDefaults.buttonColors(containerColor = Secondary)) {
            Text(
                text = "Get Started",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp.from(ctx),
                    color = Color.White
                )
            )
        }

    }
}