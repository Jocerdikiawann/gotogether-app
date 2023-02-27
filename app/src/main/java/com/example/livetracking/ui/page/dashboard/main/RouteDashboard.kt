package com.example.livetracking.ui.page.dashboard.main

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.example.livetracking.ui.page.dashboard.history.routeHistory
import com.example.livetracking.ui.page.dashboard.home.Dashboard
import com.example.livetracking.ui.page.dashboard.home.routeDashboard
import com.example.livetracking.ui.page.dashboard.main.Main.navigateToItemDashboard
import com.example.livetracking.ui.page.dashboard.saved.routeSaved

object Main {
    const val routeName = "main"
    fun NavHostController.navigateToItemDashboard(route: String) {
        this.navigate(route) {
            launchSingleTop = true
        }
    }
}

fun NavGraphBuilder.dashboardGraph(
    navHostController: NavHostController,
) {
    navigation(startDestination = Dashboard.routeName, route = Main.routeName) {
        routeHistory(onNavigateToItemDashboard = {
            navHostController.navigateToItemDashboard(it)
        })
        routeDashboard(onNavigateToItemDashboard = {
            navHostController.navigateToItemDashboard(it)
        })
        routeSaved(onNavigateToItemDashboard = {
            navHostController.navigateToItemDashboard(it)
        })
    }
}