package com.example.livetracking

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.livetracking.ui.page.dashboard.main.routeMain
import com.example.livetracking.ui.page.splash.Splash
import com.example.livetracking.ui.page.splash.routeSplash

@Composable
fun AppNavigation(
    nav: NavHostController,
    start: String = Splash.routeName,
) {
    NavHost(navController = nav, startDestination = start) {
        routeSplash(nav)
        routeMain(nav)
    }
}