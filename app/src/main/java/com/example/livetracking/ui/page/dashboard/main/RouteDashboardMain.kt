package com.example.livetracking.ui.page.dashboard.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.livetracking.ui.page.dashboard.history.History
import com.example.livetracking.ui.page.dashboard.history.PageHistory
import com.example.livetracking.ui.page.dashboard.home.Dashboard
import com.example.livetracking.ui.page.dashboard.home.PageDashboard
import com.example.livetracking.ui.page.dashboard.saved.PageSaved
import com.example.livetracking.ui.page.dashboard.saved.Saved

object Main {
    const val routeName = "main"
}

fun NavGraphBuilder.routeMain(
    nav: NavHostController
) {
    composable(Main.routeName) {
        var currentRoute by remember { mutableStateOf(Dashboard.routeName) }
        DashboardMain(onItemClick = {
            currentRoute = it
        }) {
            when (currentRoute) {
                Dashboard.routeName -> {
                    PageDashboard()
                }
                Saved.routeName -> {
                    PageSaved()
                }
                History.routeName -> {
                    PageHistory()
                }
            }
        }
    }
}