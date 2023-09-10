package com.example.livetracking.ui.page.dashboard.saved

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.FocusRequester
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.livetracking.service.LocationService
import com.example.livetracking.ui.page.dashboard.main.DashboardMain

object Saved {
    const val routeName = "saved"
}

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.routeSaved(
    onNavigateToItemDashboard: (String) -> Unit,
) {
    composable(Saved.routeName) {
        val interactionSource = MutableInteractionSource()
        val focusRequester = remember {
            FocusRequester()
        }
        val isSharing by LocationService.IS_SHARING.collectAsStateWithLifecycle(false)
        DashboardMain(
            currentRoute = it.destination.route ?: "",
            onItemClick = { route ->
                onNavigateToItemDashboard(route)
            }, isSharing = isSharing,
            gotoDirection = {}
        ) {
            PageSaved(
                interactionSource = interactionSource,
                focusRequester = focusRequester,
            )
        }
    }
}