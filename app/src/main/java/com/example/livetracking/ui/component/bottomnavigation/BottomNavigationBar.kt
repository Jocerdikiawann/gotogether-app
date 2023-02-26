package com.example.livetracking.ui.component.bottomnavigation

import android.content.Context
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.livetracking.R
import com.example.livetracking.ui.page.dashboard.history.History
import com.example.livetracking.ui.page.dashboard.home.Dashboard
import com.example.livetracking.ui.page.dashboard.saved.Saved
import com.example.livetracking.ui.theme.Primary
import com.example.livetracking.ui.theme.Tertiary
import com.example.livetracking.utils.from

@Composable
fun BottomNavigationBar(
    items: List<BottomNavigationBar> = listOf(),
    currentRoute: String = Dashboard.routeName,
    ctx: Context,
    onItemClick: (String) -> Unit = {}
) {
    NavigationBar(
        containerColor = Tertiary,
        tonalElevation = 1.dp.from(ctx),
        windowInsets = NavigationBarDefaults.windowInsets,
    ) {
        items.forEach {
            NavigationBarItem(
                selected = currentRoute == it.routes,
                onClick = { onItemClick(it.routes) },
                icon = {
                    Icon(
                        painter = painterResource(id = it.icons),
                        contentDescription = "menu_${it.name}",
                        tint = if (currentRoute == it.routes) Color.White else Color.Black
                    )
                },
                label = {
                    Text(
                        text = it.name, style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 12.sp.from(ctx),
                            fontWeight = FontWeight.Light
                        )
                    )
                },
                colors = NavigationBarItemDefaults.colors(indicatorColor = Primary)
            )
        }
    }
}

sealed class BottomNavigationBar(
    val routes: String,
    val name: String,
    val icons: Int,
) {
    class Home : BottomNavigationBar(
        routes = Dashboard.routeName,
        name = "Home",
        icons = R.drawable.ic_home
    )

    class Save : BottomNavigationBar(
        routes = Saved.routeName,
        name = "Saved",
        icons = R.drawable.ic_bookmark
    )

    class Histories : BottomNavigationBar(
        routes = History.routeName,
        name = "History",
        icons = R.drawable.ic_history
    )

}