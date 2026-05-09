package com.choiminjun.home.route

import com.choiminjun.base.UiIntent
import com.choiminjun.base.UiSideEffect
import com.choiminjun.base.UiState
import com.choiminjun.domain.model.bus.BusNode

data class BusRouteState(
    val routeNo: String = "",
    val isLoading: Boolean = false,
    val nodes: List<BusNode> = emptyList(),
) : UiState

sealed interface BusRouteIntent : UiIntent {
    data object ClickBack : BusRouteIntent
    data class ClickBusNode(val busNode: BusNode) : BusRouteIntent
}

sealed interface BusRouteSideEffect : UiSideEffect {
    data object NavigateBack : BusRouteSideEffect
    data class NavigateToBusNode(val nodeId: String, val nodeName: String, val nodeNo: String?) : BusRouteSideEffect
}
