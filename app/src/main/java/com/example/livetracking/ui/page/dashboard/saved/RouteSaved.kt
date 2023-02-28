package com.example.livetracking.ui.page.dashboard.saved

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.FocusRequester
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
        val interactionSource = MutableInteractionSource()
        val focusRequester = remember {
            FocusRequester()
        }
        DashboardMain(
            currentRoute = it.destination.route ?: "",
            onItemClick = { route ->
                onNavigateToItemDashboard(route)
            }) {
            PageSaved(
                interactionSource = interactionSource,
                focusRequester = focusRequester,
            )
        }
    }
}