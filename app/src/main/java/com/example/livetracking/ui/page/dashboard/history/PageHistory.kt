package com.example.livetracking.ui.page.dashboard.history

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

object History {
    const val routeName = "history"
}

@Composable
fun PageHistory(){
    Text(text = "Hello from history")
}