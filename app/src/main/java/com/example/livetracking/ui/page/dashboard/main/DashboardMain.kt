package com.example.livetracking.ui.page.dashboard.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.livetracking.ui.component.bottomnavigation.BottomNavigationBar
import com.example.livetracking.ui.component.topbar.ProfileBar
import com.example.livetracking.ui.page.dashboard.home.Dashboard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardMain(
    onItemClick: (String) -> Unit = {},
    currentRoute: String,
    content: @Composable () -> Unit,
) {
    val ctx = LocalContext.current
    Scaffold(
        containerColor = Color.White,
        topBar = {
        },
        bottomBar = {
            BottomNavigationBar(
                items = listOf(
                    BottomNavigationBar.Home(),
                    BottomNavigationBar.Save(),
                    BottomNavigationBar.Histories()
                ),
                currentRoute = currentRoute,
                ctx = ctx,
                onItemClick = { route ->
                    onItemClick(route)
                }
            )
        },
        contentWindowInsets = NavigationBarDefaults.windowInsets
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            content.invoke()
        }
    }
}