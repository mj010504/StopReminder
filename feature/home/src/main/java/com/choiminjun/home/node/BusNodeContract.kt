package com.choiminjun.home.node

import com.choiminjun.base.UiIntent
import com.choiminjun.base.UiSideEffect
import com.choiminjun.base.UiState
import com.choiminjun.domain.model.bus.BusRoute

data class BusNodeState(
    val nodeName: String = "",
    val nodeNo: String? = null,
    val isLoading: Boolean = false,
    val routes: List<BusRoute> = emptyList(),
) : UiState

sealed interface BusNodeIntent : UiIntent {
    data object ClickBack : BusNodeIntent
    data class ClickAlarm(val routeId: String, val routeNo: String) : BusNodeIntent
    data class ClickBusRoute(val route: BusRoute) : BusNodeIntent
}

sealed interface BusNodeSideEffect : UiSideEffect {
    data object NavigateBack : BusNodeSideEffect
    data class NavigateToAlarm(val routeId: String, val routeNo: String) : BusNodeSideEffect
    data class NavigateToBusRoute(val routeId: String, val routeNo: String) : BusNodeSideEffect
}
