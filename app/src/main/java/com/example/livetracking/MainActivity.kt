package com.example.livetracking

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.livetracking.ui.page.dashboard.home.Dashboard
import com.example.livetracking.ui.page.splash.Splash
import com.example.livetracking.ui.theme.LiveTrackingTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navHostController = rememberNavController()
            val systemUiController = rememberSystemUiController()
            val useDarkIcon = !isSystemInDarkTheme()
            val theme = Color.Transparent

            DisposableEffect(key1 = systemUiController, key2 = useDarkIcon) {
                systemUiController.setSystemBarsColor(
                    color = theme,
                    darkIcons = useDarkIcon
                )

                onDispose { }
            }

            LiveTrackingTheme {
                AppNavigation(nav = navHostController, start = Splash.routeName)
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