package com.example.livetracking.ui.page.dashboard.main

import android.os.Build
import androidx.annotation.RequiresApi
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
import com.example.livetracking.ui.page.direction.Direction
import com.example.livetracking.ui.page.search.Search

object Main {
    const val routeName = "main"
    fun NavHostController.navigateTo(route: String, navOptions: NavOptions? = null) {
        this.navigate(route, navOptions)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.dashboardGraph(
    navHostController: NavHostController,
) {
    navigation(startDestination = Dashboard.routeName, route = Main.routeName) {
        routeHistory(onNavigateToItemDashboard = {
            navHostController.navigateTo(it, navOptions = navOptions { launchSingleTop = true })
        })
        routeDashboard(
            onNavigateToItemDashboard = {
                navHostController.navigateTo(it, navOptions = navOptions { launchSingleTop = true })
            },
            onNavigateToSearchLocation = {
                navHostController.navigateTo(
                    Search.routeName,
                    navOptions = navOptions {
                        launchSingleTop = true
                    })
            },
            onNavigateToDirection = {
                navHostController.navigateTo("${Direction.routeName}/$it", navOptions {
                    launchSingleTop = true
                })
            }
        )
        routeSaved(onNavigateToItemDashboard = {
            navHostController.navigateTo(it, navOptions { launchSingleTop = true })
        })
    }
}