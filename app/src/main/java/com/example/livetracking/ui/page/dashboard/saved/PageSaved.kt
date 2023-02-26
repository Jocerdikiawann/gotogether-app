package com.example.livetracking.ui.page.dashboard.saved

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

object Saved {
    const val routeName = "saved"
}

@Composable
fun PageSaved(
    nav:NavHostController,
){
    Text(text = "Hello from saved")
}