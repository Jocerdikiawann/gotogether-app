package com.example.livetracking.ui.page.dashboard.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.livetracking.ui.page.dashboard.main.DashboardMain

object Dashboard {
    const val routeName: String = "dashboard"
}

fun NavGraphBuilder.routeDashboard(
    router: NavHostController
) {
    composable(Dashboard.routeName) {
        DashboardMain(
            currentRoute = it.destination.route ?: Dashboard.routeName,
            onItemClick = { route ->
                router.navigate(route) {
                    launchSingleTop = true
                }
            }) {
            PageDashboard()
        }

    }
}