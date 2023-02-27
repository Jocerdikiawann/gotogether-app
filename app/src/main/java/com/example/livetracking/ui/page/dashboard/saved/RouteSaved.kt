package com.example.livetracking.ui.page.dashboard.saved

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.livetracking.ui.page.dashboard.main.DashboardMain

object Saved {
    const val routeName = "saved"
}

fun NavGraphBuilder.routeSaved(
    onNavigateToItemDashboard: (String) -> Unit,
) {
    composable(Saved.routeName) {
        DashboardMain(
            currentRoute = it.destination.route ?: "",
            onItemClick = { route ->
                onNavigateToItemDashboard(route)
            }) {
            PageSaved()
        }
    }
}