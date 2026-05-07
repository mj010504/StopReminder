package com.choiminjun.home.home

import com.choiminjun.base.UiIntent
import com.choiminjun.base.UiSideEffect
import com.choiminjun.base.UiState
import com.choiminjun.domain.model.bus.BusNode
import com.choiminjun.domain.model.bus.BusRoute

enum class SearchTab { BUS, STOP }

data class HomeState(
    val searchQuery: String = "",
    val isSearching: Boolean = false,
    val isLoading: Boolean = false,
    val searchedRoutes: List<BusRoute> = emptyList(),
    val searchedNodes: List<BusNode> = emptyList(),
    val selectedTab: SearchTab = SearchTab.BUS,
) : UiState

sealed interface HomeIntent : UiIntent {
    data class UpdateQuery(val query: String) : HomeIntent
    data object FocusSearch : HomeIntent
    data class ClickBusRoute(val routeId: String) : HomeIntent
    data class ClickBusNode(val nodeId: String) : HomeIntent
    data object ClickBack : HomeIntent
    data object ClearQuery : HomeIntent
    data class SelectTab(val tab: SearchTab) : HomeIntent
}

sealed interface HomeSideEffect : UiSideEffect {
    data class NavigateToBusRoute(val routeId: String) : HomeSideEffect
    data class NavigateToBusNode(val nodeId: String) : HomeSideEffect
}
