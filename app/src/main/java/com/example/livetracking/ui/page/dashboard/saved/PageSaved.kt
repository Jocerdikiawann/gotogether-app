package com.example.livetracking.ui.page.dashboard.saved

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.livetracking.ui.component.card.CardColumnSavedLocation
import com.example.livetracking.ui.component.textfield.TextFieldSearch
import com.example.livetracking.utils.from


@Composable
fun PageSaved(modifier: Modifier = Modifier) {
    val ctx = LocalContext.current
    LazyColumn(modifier = modifier
        .fillMaxSize(), content = {
        item {
            Spacer(modifier = modifier.padding(top = 15.dp.from(ctx)))
            TextFieldSearch(
                onValueChange = {},
                enable = true,
                readOnly = false,
                placeHolder = "Search here...",
                value = "",
                context = ctx
            )
            Spacer(modifier = modifier.height(5.dp.from(ctx)))
        }
        items(5) {
            CardColumnSavedLocation(ctx = ctx)
        }
    })
}