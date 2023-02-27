package com.example.livetracking

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.livetracking.ui.page.dashboard.history.routeHistory
import com.example.livetracking.ui.page.dashboard.home.routeDashboard
import com.example.livetracking.ui.page.dashboard.saved.routeSaved
import com.example.livetracking.ui.page.splash.Splash
import com.example.livetracking.ui.page.splash.routeSplash

@Composable
fun AppNavigation(
    nav: NavHostController,
    start: String = Splash.routeName,
) {
    NavHost(navController = nav, startDestination = start) {
        routeSplash(nav)
        routeHistory(nav)
        routeDashboard(nav)
        routeSaved(nav)
    }
}