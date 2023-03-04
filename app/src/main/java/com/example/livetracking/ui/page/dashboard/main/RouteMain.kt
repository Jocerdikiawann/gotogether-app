package com.example.livetracking.ui.page.dashboard.main

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.navOptions
import androidx.navigation.navigation
import com.example.livetracking.ui.page.dashboard.history.routeHistory
import com.example.livetracking.ui.page.dashboard.home.Dashboard
import com.example.livetracking.ui.page.dashboard.home.routeDashboard
import com.example.livetracking.ui.page.dashboard.main.Main.navigateTo
import com.example.livetracking.ui.page.dashboard.saved.routeSaved
import com.example.livetracking.ui.page.search.Search
import com.google.gson.Gson

object Main {
    const val routeName = "main"
    fun NavHostController.navigateTo(route: String, navOptions: NavOptions? = null) {
        this.navigate(route, navOptions)
    }
}

fun NavGraphBuilder.dashboardGraph(
    navHostController: NavHostController,
) {
    navigation(startDestination = Dashboard.routeName, route = Main.routeName) {
        routeHistory(onNavigateToItemDashboard = {
            navHostController.navigateTo(it, navOptions = navOptions { launchSingleTop = true })
        })
        routeDashboard(onNavigateToItemDashboard = {
            navHostController.navigateTo(it, navOptions = navOptions { launchSingleTop = true })
        }, onNavigateToSearchLocation = {
            val gson = Gson()
            val data = gson.toJson(it)
            navHostController.navigateTo(
                "${Search.routeName}/$data",
                navOptions = navOptions {
                    launchSingleTop = true
                })
        })
        routeSaved(onNavigateToItemDashboard = {
            navHostController.navigateTo(it, navOptions { launchSingleTop = true })
        })
    }
}