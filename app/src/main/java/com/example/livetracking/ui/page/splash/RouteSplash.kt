package com.example.livetracking.ui.page.splash

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.livetracking.ui.page.auth.Auth
import com.example.livetracking.ui.page.dashboard.main.Main
import com.example.livetracking.ui.page.splash.Splash.navigateToAuth
import com.example.livetracking.ui.page.splash.Splash.navigateToHome

object Splash {
    const val routeName = "splash"

    fun NavHostController.navigateToAuth() {
        navigate(Auth.routeName) {
            popUpTo(graph.findStartDestination().id) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }

    fun NavHostController.navigateToHome() {
        navigate(Main.routeName) {
            popUpTo(graph.findStartDestination().id) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }
}

fun NavGraphBuilder.routeSplash(
    router: NavHostController,
) {
    composable(Splash.routeName) {
        val viewModel = hiltViewModel<ViewModelSplash>()
        val isLoggedIn by viewModel.isLoggedIn.collectAsState(initial = false)
        LaunchedEffect(key1 = true, block = {
            with(router) {
                if (isLoggedIn) {
                    navigateToHome()
                } else {
                    navigateToAuth()
                }
            }
        })
        SplashScreen()
    }
}