package com.example.livetracking.ui.page.dashboard.history

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.livetracking.service.LocationService
import com.example.livetracking.ui.page.dashboard.main.DashboardMain

object History {
    const val routeName = "history"
}


@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.routeHistory(
    onNavigateToItemDashboard: (String) -> Unit,
) {
    composable(History.routeName) {
        val isSharing by LocationService.IS_SHARING.collectAsStateWithLifecycle(false)
        DashboardMain(
            currentRoute = it.destination.route ?: "",
            onItemClick = { route ->
                onNavigateToItemDashboard(route)
            },
            gotoDirection = {},
            isSharing = isSharing,
        ) {
            PageHistory()
        }
    }
}
