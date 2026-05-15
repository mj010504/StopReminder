package com.choiminjun.home.home.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.choiminjun.designsystem.theme.SRTheme
import com.choiminjun.designsystem.theme.Spacing
import com.choiminjun.domain.model.bus.BusNode
import com.choiminjun.domain.model.bus.BusRoute
import com.choiminjun.domain.model.search.RecentNodeSearch
import com.choiminjun.domain.model.search.RecentRouteSearch
import com.choiminjun.home.R
import com.choiminjun.home.home.SearchTab

@Composable
internal fun SearchSection(
    isLoading: Boolean,
    searchQuery: String,
    searchedRoutes: List<BusRoute>,
    searchedNodes: List<BusNode>,
    selectedTab: SearchTab,
    recentRouteSearches: List<RecentRouteSearch>,
    recentNodeSearches: List<RecentNodeSearch>,
    onBusRouteClick: (BusRoute) -> Unit,
    onBusNodeClick: (BusNode) -> Unit,
    onTabSelect: (SearchTab) -> Unit,
    onRecentRouteSearchDelete: (Long) -> Unit,
    onRecentNodeSearchDelete: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        SearchTabRow(selectedTab = selectedTab, onTabSelect = onTabSelect)
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = SRTheme.colors.coolNeutral95,
        )
        if (isLoading) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(modifier = Modifier.imePadding(), color = SRTheme.colors.blue50)
            }
        } else {
            SearchResult(
                modifier = Modifier.weight(1f),
                query = searchQuery,
                routes = searchedRoutes,
                nodes = searchedNodes,
                selectedTab = selectedTab,
                recentRouteSearches = recentRouteSearches,
                recentNodeSearches = recentNodeSearches,
                onBusRouteClick = onBusRouteClick,
                onBusNodeClick = onBusNodeClick,
                onRecentRouteSearchDelete = onRecentRouteSearchDelete,
                onRecentNodeSearchDelete = onRecentNodeSearchDelete,
            )
        }
    }
}

@Composable
private fun SearchTabRow(
    selectedTab: SearchTab,
    onTabSelect: (SearchTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    val selectedColor = SRTheme.colors.textPrimary
    Row(modifier = modifier.fillMaxWidth()) {
        SearchTab.entries.forEach { tab ->
            val isSelected = tab == selectedTab
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onTabSelect(tab) }
                    .drawBehind {
                        if (isSelected) {
                            val strokeWidth = 2.dp.toPx()
                            drawLine(
                                color = selectedColor,
                                start = Offset(0f, size.height),
                                end = Offset(size.width, size.height),
                                strokeWidth = strokeWidth,
                            )
                        }
                    }
                    .padding(vertical = Spacing.space12),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = when (tab) {
                        SearchTab.BUS -> stringResource(R.string.search_tab_bus)
                        SearchTab.STOP -> stringResource(R.string.search_tab_stop)
                    },
                    style = SRTheme.typography.bodyMM,
                    color = if (isSelected) SRTheme.colors.textPrimary else SRTheme.colors.textSecondary,
                )
            }
        }
    }
}
