package com.example.livetracking.ui.page.splash

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.livetracking.ui.page.dashboard.main.Main
import com.example.livetracking.ui.page.splash.Splash.navigateToHome

object Splash {
    const val routeName = "splash"

    fun NavHostController.navigateToHome() {
        navigate(Main.routeName) {
            popUpTo(graph.findStartDestination().id) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }
}

fun NavGraphBuilder.routeSplash(
    router: NavHostController,
) {
    composable(Splash.routeName) {
        LaunchedEffect(key1 = true, block = {
            with(router) {
                navigateToHome()
            }
        })
        SplashScreen()
    }
}