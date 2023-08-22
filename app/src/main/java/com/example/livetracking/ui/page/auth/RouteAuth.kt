package com.example.livetracking.ui.page.auth

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.livetracking.data.utils.AuthResultContract
import com.example.livetracking.ui.page.splash.Splash.navigateToHome
import com.example.livetracking.utils.toast

object Auth {
    const val routeName = "auth"
}

fun NavGraphBuilder.routeAuth(
    navHostController: NavHostController,
) {
    composable(Auth.routeName) {
        val context = LocalContext.current
        val viewModel = hiltViewModel<ViewModelAuth>()
        val authStateUI by viewModel.authStateUI.collectAsState(initial = AuthStateUI())
        val authResultLauncher = rememberLauncherForActivityResult(
            contract = AuthResultContract(),
        ) { task ->
            viewModel.authentication(task)
        }
        val activity = (LocalContext.current as? Activity)

        LaunchedEffect(key1 = authStateUI.success) {
            if (authStateUI.success) {
                with(navHostController) {
                    navigateToHome()
                }
            }
            if (authStateUI.error) {
                context.toast(authStateUI.errMsg)
            }
        }
        PageAuth(loading = authStateUI.loading, onSignIn = {
            authResultLauncher.launch(1)
        }) {
            activity?.finish()
        }
    }
}