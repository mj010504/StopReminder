package com.choiminjun.home.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.choiminjun.designsystem.component.SRIconButton
import com.choiminjun.designsystem.theme.SRTheme
import com.choiminjun.designsystem.theme.Spacing
import com.choiminjun.domain.model.bus.BusNode
import com.choiminjun.domain.model.bus.BusRoute
import com.choiminjun.domain.model.bus.CityCode
import com.choiminjun.home.R
import com.choiminjun.home.home.component.BoardingBanner
import com.choiminjun.home.home.component.SearchField
import com.choiminjun.home.home.component.SearchResult
import com.choiminjun.designsystem.R as DesignSystemR

@Composable
internal fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel(),
    navigateToBusRoute: (routeId: String, routeNo: String) -> Unit,
    navigateToBusNode: (nodeId: String, nodeName: String, nodeNo: String?) -> Unit,
    navigateToAlarmRing: () -> Unit,
) {
    val state by viewModel.collectAsState()

    viewModel.collectSideEffect { effect ->
        when (effect) {
            is HomeSideEffect.NavigateToBusRoute -> navigateToBusRoute(effect.routeId, effect.routeNo)
            is HomeSideEffect.NavigateToBusNode -> navigateToBusNode(effect.nodeId, effect.nodeName, effect.nodeNo)
            HomeSideEffect.NavigateToAlarmRing -> navigateToAlarmRing()
        }
    }

    HomeScreen(
        state = state,
        onBackClick = { viewModel.onIntent(HomeIntent.ClickBack) },
        onQueryChange = { query -> viewModel.onIntent(HomeIntent.UpdateQuery(query)) },
        onSearchFocused = { viewModel.onIntent(HomeIntent.FocusSearch) },
        onBusRouteClick = { busRoute -> viewModel.onIntent(HomeIntent.ClickBusRoute(busRoute)) },
        onBusNodeClick = { busNode -> viewModel.onIntent(HomeIntent.ClickBusNode(busNode)) },
        onTabSelect = { tab -> viewModel.onIntent(HomeIntent.SelectTab(tab)) },
        onQueryClear = { viewModel.onIntent(HomeIntent.ClearQuery) },
        onRecentRouteSearchDelete = { id -> viewModel.onIntent(HomeIntent.DeleteRecentRouteSearch(id)) },
        onRecentNodeSearchDelete = { id -> viewModel.onIntent(HomeIntent.DeleteRecentNodeSearch(id)) },
        onAlarmBannerClick = { viewModel.onIntent(HomeIntent.ClickAlarmBanner) },
        onAlarmCancelClick = { viewModel.onIntent(HomeIntent.CancelAlarm) },
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun HomeScreen(
    state: HomeState,
    onBackClick: () -> Unit,
    onQueryChange: (String) -> Unit,
    onSearchFocused: () -> Unit,
    onBusRouteClick: (BusRoute) -> Unit,
    onBusNodeClick: (BusNode) -> Unit,
    onTabSelect: (SearchTab) -> Unit,
    onQueryClear: () -> Unit,
    onRecentRouteSearchDelete: (Long) -> Unit,
    onRecentNodeSearchDelete: (Long) -> Unit,
    onAlarmBannerClick: () -> Unit,
    onAlarmCancelClick: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val imeVisible = WindowInsets.isImeVisible
    val handleBack: () -> Unit = {
        if (imeVisible) keyboardController?.hide() else onBackClick()
    }

    BackHandler(enabled = imeVisible || state.isSearching) {
        handleBack()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SRTheme.colors.background)
            .statusBarsPadding()
            .navigationBarsPadding(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.space20, vertical = Spacing.space16),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.space16),
        ) {
            SRIconButton(
                imageVector = if (state.isSearching) {
                    ImageVector.vectorResource(DesignSystemR.drawable.ic_arrow_left)
                } else {
                    ImageVector.vectorResource(DesignSystemR.drawable.ic_menu)
                },
                contentDescription = if (state.isSearching) "뒤로가기" else "메뉴",
                onClick = { handleBack() },
            )
            SearchField(
                value = state.searchQuery,
                onValueChange = onQueryChange,
                onFocused = onSearchFocused,
                onClearClick = onQueryClear,
            )
        }
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = SRTheme.colors.coolNeutral95,
        )

        if (state.isSearching) {
            SearchTabRow(
                selectedTab = state.selectedTab,
                onTabSelect = onTabSelect,
            )
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = SRTheme.colors.coolNeutral95,
            )

            if (state.isLoading) {
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
                    query = state.searchQuery,
                    routes = state.searchedRoutes,
                    nodes = state.searchedNodes,
                    selectedTab = state.selectedTab,
                    recentRouteSearches = state.recentRouteSearches,
                    recentNodeSearches = state.recentNodeSearches,
                    onBusRouteClick = onBusRouteClick,
                    onBusNodeClick = onBusNodeClick,
                    onRecentRouteSearchDelete = onRecentRouteSearchDelete,
                    onRecentNodeSearchDelete = onRecentNodeSearchDelete,
                )
            }
        } else {
            state.alarmInfo?.let { alarmInfo ->
                // FIXME: 현재는 배너 클릭 시 AlarmRingScreen으로 진입.
                //        실제로는 버스가 목적지에 근접할 때 알람 이벤트로 강제 전환해야 함.
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Spacing.space20, vertical = Spacing.space16),
                ) {
                    BoardingBanner(
                        routeNo = alarmInfo.routeNo,
                        destNodeName = alarmInfo.destNodeName,
                        stopsBeforeAlarm = alarmInfo.stopsBeforeAlarm,
                        onClick = onAlarmBannerClick,
                        onCancelClick = onAlarmCancelClick,
                    )
                }
            }
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

@Composable
@Preview(showBackground = true)
private fun HomeScreenPreview() {
    SRTheme {
        HomeScreen(
            state = HomeState(),
            onBackClick = {},
            onQueryChange = {},
            onSearchFocused = {},
            onBusRouteClick = {},
            onBusNodeClick = {},
            onTabSelect = {},
            onQueryClear = {},
            onRecentRouteSearchDelete = {},
            onRecentNodeSearchDelete = {},
            onAlarmBannerClick = {},
            onAlarmCancelClick = {},
        )
    }
}

@Composable
@Preview(showBackground = true, name = "검색 전")
private fun HomeScreenPrevSearchPreview() {
    SRTheme {
        HomeScreen(
            state = HomeState(
                isSearching = true,
            ),
            onBackClick = {},
            onQueryChange = {},
            onSearchFocused = {},
            onBusRouteClick = {},
            onBusNodeClick = {},
            onTabSelect = {},
            onQueryClear = {},
            onRecentRouteSearchDelete = {},
            onRecentNodeSearchDelete = {},
            onAlarmBannerClick = {},
            onAlarmCancelClick = {},
        )
    }
}

@Composable
@Preview(showBackground = true, name = "검색 중 - 버스 탭")
private fun HomeScreenSearchBusTabPreview() {
    SRTheme {
        HomeScreen(
            state = HomeState(
                searchQuery = "51",
                isSearching = true,
                selectedTab = SearchTab.BUS,
                searchedRoutes = listOf(
                    BusRoute(
                        routeId = "1",
                        routeNo = "51",
                        routeType = "일반버스",
                        startNodeName = "노포동",
                        endNodeName = "하단",
                        cityCode = CityCode.BUSAN,
                    ),
                    BusRoute(
                        routeId = "2",
                        routeNo = "51-1",
                        routeType = "급행버스",
                        startNodeName = "기장",
                        endNodeName = "사상",
                        cityCode = CityCode.BUSAN,
                    ),
                ),
            ),
            onBackClick = {},
            onQueryChange = {},
            onSearchFocused = {},
            onBusRouteClick = {},
            onBusNodeClick = {},
            onTabSelect = {},
            onQueryClear = {},
            onRecentRouteSearchDelete = {},
            onRecentNodeSearchDelete = {},
            onAlarmBannerClick = {},
            onAlarmCancelClick = {},
        )
    }
}

@Composable
@Preview(showBackground = true, name = "검색 중 - 정류장 탭")
private fun HomeScreenSearchStopTabPreview() {
    SRTheme {
        HomeScreen(
            state = HomeState(
                searchQuery = "부산대",
                isSearching = true,
                selectedTab = SearchTab.STOP,
                searchedNodes = listOf(
                    BusNode(
                        nodeId = "N1",
                        nodeName = "부산대학교",
                        latitude = 35.23,
                        longitude = 129.08,
                        cityCode = CityCode.BUSAN,
                    ),
                    BusNode(
                        nodeId = "N2",
                        nodeName = "부산대앞",
                        latitude = 35.23,
                        longitude = 129.09,
                        cityCode = CityCode.BUSAN,
                    ),
                ),
            ),
            onBackClick = {},
            onQueryChange = {},
            onSearchFocused = {},
            onBusRouteClick = {},
            onBusNodeClick = {},
            onTabSelect = {},
            onQueryClear = {},
            onRecentRouteSearchDelete = {},
            onRecentNodeSearchDelete = {},
            onAlarmBannerClick = {},
            onAlarmCancelClick = {},
        )
    }
}
