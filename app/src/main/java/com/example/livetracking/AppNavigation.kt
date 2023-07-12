package com.example.livetracking

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.livetracking.ui.page.auth.routeAuth
import com.example.livetracking.ui.page.dashboard.main.dashboardGraph
import com.example.livetracking.ui.page.direction.routeDirection
import com.example.livetracking.ui.page.search.routeSearch
import com.example.livetracking.ui.page.splash.Splash
import com.example.livetracking.ui.page.splash.routeSplash

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun AppNavigation(
    nav: NavHostController,
    start: String = Splash.routeName,
) {
    NavHost(navController = nav, startDestination = start) {
        routeSplash(nav)
        dashboardGraph(nav)
        routeSearch(nav)
        routeDirection(nav)
        routeAuth(nav)
    }
}