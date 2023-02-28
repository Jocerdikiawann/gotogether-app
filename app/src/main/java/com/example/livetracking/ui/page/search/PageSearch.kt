package com.example.livetracking.ui.page.search

import android.content.Context
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.unit.dp
import com.example.livetracking.ui.component.card.CardRecentHistory
import com.example.livetracking.ui.component.textfield.TextFieldSearch
import com.example.livetracking.ui.component.topbar.BaseTopBar
import com.example.livetracking.utils.from

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageSearch(
    modifier: Modifier = Modifier,
    context: Context,
    valueTextSearch: String,
    onValueChange: (String) -> Unit,
    focusRequester: FocusRequester,
    interactionSource: MutableInteractionSource,
    onBackStack:()->Unit,
) {
    val listItem = listOf<Int>(1, 2, 3)
    Scaffold(
        topBar = {
            BaseTopBar(context = context, title = "Search Location") {
                onBackStack()
            }
        },
    ) {
        LazyColumn(modifier = modifier.padding(it), content = {
            item {
                Box(modifier = modifier.padding(vertical = 10.dp.from(context))) {
                    TextFieldSearch(
                        onValueChange = { onValueChange(it) },
                        enable = true,
                        readOnly = false,
                        placeHolder = "Find your destination...",
                        value = valueTextSearch,
                        context = context,
                        focusRequester = focusRequester,
                        interactionSource = interactionSource,
                    )
                }
            }
            items(listItem.size) { index ->
                CardRecentHistory(
                    context = context,
                    onClickAction = {}
                )
                if (index != listItem.lastIndex) Divider(
                    modifier = modifier.padding(
                        vertical = 10.dp.from(context),
                        horizontal = 12.dp.from(context)
                    )
                )
            }
        })
    }
}