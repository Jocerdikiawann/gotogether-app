package com.example.livetracking.ui.page.dashboard

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageDashboard (
    modifier:Modifier=Modifier
){
    Scaffold(topBar = {}, bottomBar = {}) {
        LazyColumn(modifier=modifier.padding(it),content = {
            item {

            }
        })
    }
}