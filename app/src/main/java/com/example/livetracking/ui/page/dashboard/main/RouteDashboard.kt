package com.example.livetracking.ui.page.dashboard.main

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.example.livetracking.ui.page.dashboard.history.routeHistory
import com.example.livetracking.ui.page.dashboard.home.Dashboard
import com.example.livetracking.ui.page.dashboard.home.routeDashboard
import com.example.livetracking.ui.page.dashboard.main.Main.navigateTo
import com.example.livetracking.ui.page.dashboard.saved.routeSaved
import com.example.livetracking.ui.page.search.Search

object Main {
    const val routeName = "main"
    fun NavHostController.navigateTo(route: String) {
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
            navHostController.navigateTo(it)
        })
        routeDashboard(onNavigateToItemDashboard = {
            navHostController.navigateTo(it)
        }, onNavigateToSearchLocation = {
            navHostController.navigateTo(Search.routeName)
        })
        routeSaved(onNavigateToItemDashboard = {
            navHostController.navigateTo(it)
        })
    }
}