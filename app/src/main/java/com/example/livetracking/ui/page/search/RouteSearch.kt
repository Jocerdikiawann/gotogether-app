package com.example.livetracking.ui.page.search

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable

object Search {
    const val routeName = "search"
}

fun NavGraphBuilder.routeSearch(
    navHostController: NavHostController,
) {
    composable(Search.routeName) {
        val context = LocalContext.current
        val focusRequester = remember {
            FocusRequester()
        }
        val interactionSource = MutableInteractionSource()
        var textSearch by remember { mutableStateOf("") }

        LaunchedEffect(key1 = true, block = {
            focusRequester.requestFocus()
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
                navHostController.popBackStack()
            }
        )
    }
}