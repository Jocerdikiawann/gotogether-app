package com.example.livetracking.ui.page.dashboard.history

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.livetracking.ui.page.dashboard.main.DashboardMain

object History {
    const val routeName = "history"
}


fun NavGraphBuilder.routeHistory(
    onNavigateToItemDashboard:(String)->Unit,
) {
    composable(History.routeName) {
        DashboardMain(
            currentRoute = it.destination.route ?: "",
            onItemClick = { route ->
                onNavigateToItemDashboard(route)
            }) {
            PageHistory()
        }
    }
}
