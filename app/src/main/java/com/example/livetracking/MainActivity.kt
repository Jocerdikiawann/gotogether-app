package com.example.livetracking

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.livetracking.ui.page.dashboard.Dashboard
import com.example.livetracking.ui.theme.LiveTrackingTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import androidx.core.view.WindowCompat
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navHostController = rememberNavController()
            val systemUiController = rememberSystemUiController()
            val useDarkIcon = !isSystemInDarkTheme()
            val theme = MaterialTheme.colorScheme.onBackground

            SideEffect {
                systemUiController.setSystemBarsColor(
                    color = theme,
                    darkIcons =useDarkIcon
                )
            }
            LiveTrackingTheme {
                AppNavigation(nav = navHostController, start = Dashboard.routeName)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LiveTrackingTheme {

    }
}