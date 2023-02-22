package com.example.livetracking

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.livetracking.ui.page.dashboard.Dashboard
import com.example.livetracking.ui.page.dashboard.routeDashboard

@Composable
fun AppNavigation(
    nav: NavHostController,
    start: String = Dashboard.routeName,
) {
    NavHost(navController = nav, startDestination = start) {
        routeDashboard(nav)
    }
}