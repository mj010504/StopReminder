package com.choiminjun.home.home

import com.choiminjun.base.UiIntent
import com.choiminjun.base.UiSideEffect
import com.choiminjun.base.UiState
import com.choiminjun.domain.model.alarm.AlarmInfo
import com.choiminjun.domain.model.bus.BusNode
import com.choiminjun.domain.model.bus.BusRoute
import com.choiminjun.domain.model.search.RecentNodeSearch
import com.choiminjun.domain.model.search.RecentRouteSearch

enum class SearchTab { BUS, STOP }

data class HomeState(
    val searchQuery: String = "",
    val isSearching: Boolean = false,
    val isLoading: Boolean = false,
    val searchedRoutes: List<BusRoute> = emptyList(),
    val searchedNodes: List<BusNode> = emptyList(),
    val selectedTab: SearchTab = SearchTab.BUS,
    val recentRouteSearches: List<RecentRouteSearch> = emptyList(),
    val recentNodeSearches: List<RecentNodeSearch> = emptyList(),
    val alarmInfo: AlarmInfo? = null,
) : UiState

sealed interface HomeIntent : UiIntent {
    data class UpdateQuery(val query: String) : HomeIntent
    data object FocusSearch : HomeIntent
    data class ClickBusRoute(val busRoute: BusRoute) : HomeIntent
    data class ClickBusNode(val busNode: BusNode) : HomeIntent
    data object ClickBack : HomeIntent
    data object ClearQuery : HomeIntent
    data class SelectTab(val tab: SearchTab) : HomeIntent
    data class DeleteRecentRouteSearch(val id: Long) : HomeIntent
    data class DeleteRecentNodeSearch(val id: Long) : HomeIntent
    data object ClickAlarmBanner : HomeIntent
    data object CancelAlarm : HomeIntent
}

sealed interface HomeSideEffect : UiSideEffect {
    data class NavigateToBusRoute(val routeId: String, val routeNo: String) : HomeSideEffect
    data class NavigateToBusNode(val nodeId: String, val nodeName: String, val nodeNo: String?) : HomeSideEffect
    data object NavigateToAlarmRing : HomeSideEffect
}
