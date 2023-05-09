package com.example.livetracking.ui.page.search

import android.content.Context
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.unit.dp
import com.example.livetracking.ui.component.card.CardRecentHistory
import com.example.livetracking.ui.component.card.CardRecentHistoryShimmer
import com.example.livetracking.ui.component.textfield.TextFieldSearch
import com.example.livetracking.ui.component.topbar.BaseTopBar
import com.example.livetracking.utils.from
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place

data class SearchStateUI(
    val loading: Boolean = false,
    val error: Boolean = false,
    val errMsg: String = "",
    val data: List<SearchResultState> = listOf()
)

data class SearchResultState(
    val fullAddress: String = "",
    val primaryText: String = "",
    val secondaryText: String = "",
    val placeId: String = "",
    val placeTypes: List<Place.Type> = listOf(),
    val distanceMeters: String = "",
    val isHistory:Boolean= false,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageSearch(
    modifier: Modifier = Modifier,
    resultList: SearchStateUI,
    context: Context,
    valueTextSearch: String,
    onValueChange: (String) -> Unit,
    focusRequester: FocusRequester,
    interactionSource: MutableInteractionSource,
    onNavigateToDirection: (
        namePlace: String,
        address: String,
        distance: String,
        placeId: String,
    ) -> Unit,
    onBackStack: () -> Unit,
) {
    Scaffold(
        topBar = {
            Column(modifier = modifier.fillMaxWidth()) {
                BaseTopBar(context = context, title = "Search Location") {
                    onBackStack()
                }
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

        },
    ) {
        LazyColumn(
            modifier = modifier
                .padding(it)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
            content = {
                val dataWhenLoading = listOf(1, 2, 3, 4, 5)
                if (!resultList.loading && !resultList.error) {
                    itemsIndexed(resultList.data) { index, result ->
                        CardRecentHistory(
                            context = context,
                            onClickAction = {
                                onNavigateToDirection(result.primaryText,result.fullAddress,result.distanceMeters,result.placeId)
                            },
                            fullAddress = result.fullAddress,
                            title = result.primaryText,
                            distance = result.distanceMeters,
                            isHistory = result.isHistory
                        )
                        if (index != resultList.data.lastIndex) Divider(
                            modifier = modifier.padding(
                                vertical = 10.dp.from(context),
                                horizontal = 12.dp.from(context)
                            )
                        )
                    }
                } else if (resultList.loading) {
                    itemsIndexed(dataWhenLoading) { index, _ ->
                        CardRecentHistoryShimmer(context = context)
                        if (index != dataWhenLoading.lastIndex) Divider(
                            modifier = modifier.padding(
                                vertical = 10.dp.from(context),
                                horizontal = 12.dp.from(context)
                            )
                        )
                    }
                }
            })
    }

}