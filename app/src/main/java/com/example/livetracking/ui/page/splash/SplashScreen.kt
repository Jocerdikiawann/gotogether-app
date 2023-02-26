package com.example.livetracking.ui.page.splash

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.livetracking.R
import com.example.livetracking.ui.theme.Primary
import com.example.livetracking.utils.from

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
) {
    val ctx = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = "ic_logo",
            tint = Primary,
            modifier = modifier.size(200.dp.from(ctx))
        )
        Text(
            text = "GoTogether", style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 20.sp.from(ctx),
                fontWeight = FontWeight.Bold,
            )
        )
    }

}