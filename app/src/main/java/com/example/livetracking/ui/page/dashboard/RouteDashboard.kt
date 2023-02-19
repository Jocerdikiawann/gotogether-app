package com.example.livetracking.ui.page.dashboard

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable

object Dashboard {
    const val routeName = "dashboard"
}

fun NavGraphBuilder.routeDashboard(
    nav: NavHostController
){
    composable(Dashboard.routeName){
        PageDashboard()
    }
}