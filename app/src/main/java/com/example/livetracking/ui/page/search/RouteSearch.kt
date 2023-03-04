package com.example.livetracking.ui.page.search

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.livetracking.ui.page.direction.Direction
import com.example.livetracking.ui.page.search.Search.locationArgs
import com.example.livetracking.ui.page.search.Search.navigateToDirection
import kotlinx.coroutines.delay

object Search {
    const val routeName = "search"
    const val locationArgs = "location"

    fun NavHostController.navigateToDirection() {
        navigate(Direction.routeName) {
            popUpTo(routeName) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }

    internal class SearchArgs(val location: String) {
        constructor(savedStateHandle: SavedStateHandle) :
                this(checkNotNull(savedStateHandle[locationArgs]) as String)
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun NavGraphBuilder.routeSearch(
    navHostController: NavHostController,
) {
    composable(
        "${Search.routeName}/{$locationArgs}",
        arguments = listOf(navArgument(locationArgs) {
            type = NavType.StringType
        })
    ) {
        val context = LocalContext.current
        val focusRequester = remember {
            FocusRequester()
        }
        val interactionSource = MutableInteractionSource()
        var textSearch by remember { mutableStateOf("") }

        val viewModel = hiltViewModel<ViewModelSearch>()
        val searchStateUI by viewModel.searchResultStateUI.observeAsState(SearchStateUI())

        LaunchedEffect(key1 = true, block = {
            focusRequester.requestFocus()
        })

        LaunchedEffect(key1 = textSearch, block = {
            delay(800)
            if (textSearch.isNotBlank()) {
                viewModel.getCompleteLocation(textSearch)
            }
        })

        PageSearch(
            focusRequester = focusRequester,
            context = context,
            interactionSource = interactionSource,
            valueTextSearch = textSearch,
            onValueChange = {
                textSearch = it
            },
            onBackStack = {
                with(navHostController) {
                    popBackStack()
                }
            },
            onNavigateToDirection = {
                with(navHostController) {
                    navigateToDirection()
                }
            },
            resultList = searchStateUI
        )
    }
}