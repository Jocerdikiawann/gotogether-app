package com.example.livetracking.ui.page.dashboard.saved

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.livetracking.ui.page.dashboard.main.DashboardMain

object Saved {
    const val routeName = "saved"
}

fun NavGraphBuilder.routeSaved(
    router: NavHostController,
) {
    composable(Saved.routeName) {
        DashboardMain(
            currentRoute = it.destination.route ?: Saved.routeName,
            onItemClick = { route ->
                router.navigate(route) {
                    launchSingleTop = true
                }
            }) {
            PageSaved()
        }

    }
}