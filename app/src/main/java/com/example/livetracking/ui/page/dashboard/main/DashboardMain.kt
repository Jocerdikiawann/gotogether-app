package com.example.livetracking.ui.page.dashboard.main

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.livetracking.service.LocationService
import com.example.livetracking.ui.component.bottomnavigation.BottomNavigationBar
import com.example.livetracking.utils.from

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DashboardMain(
    onItemClick: (String) -> Unit = {},
    currentRoute: String,
    isSharing:Boolean,
    gotoDirection:()->Unit,
    content: @Composable () -> Unit,
) {
    val ctx = LocalContext.current
    Scaffold(
        containerColor = Color.White,
        topBar = {
        },
        bottomBar = {
            Column {
                if (isSharing) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp.from(ctx)),
                        shape = RoundedCornerShape(20.dp.from(ctx))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp.from(ctx)),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "You are currently sharing a location.\nwith others.",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )
                            Button(
                                onClick = gotoDirection,
                                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                            ) {
                                Text(
                                    text = "Let's see",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
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
            }
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