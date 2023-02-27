package com.example.livetracking.ui.page.dashboard.history

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.livetracking.ui.page.dashboard.main.DashboardMain

object History {
    const val routeName = "history"
}


fun NavGraphBuilder.routeHistory(
    router: NavHostController
) {
    composable(History.routeName) {
        DashboardMain(
            currentRoute = it.destination.route ?: History.routeName,
            onItemClick = { route ->
                router.navigate(route) {
                    launchSingleTop = true
                }
            }) {
            PageHistory()
        }
    }
}
