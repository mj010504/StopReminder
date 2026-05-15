package com.choiminjun.home.route

import com.choiminjun.base.UiIntent
import com.choiminjun.base.UiSideEffect
import com.choiminjun.base.UiState
import com.choiminjun.domain.model.bus.BusNode

data class BusRouteState(
    val routeId: String = "",
    val routeNo: String = "",
    val routeType: String = "",
    val startNodeName: String = "",
    val endNodeName: String = "",
    val cityCode: String = "",
    val isLoading: Boolean = false,
    val nodes: List<BusNode> = emptyList(),
    val isFavorite: Boolean = false,
) : UiState

sealed interface BusRouteIntent : UiIntent {
    data object ClickBack : BusRouteIntent
    data class ClickBusNode(val busNode: BusNode) : BusRouteIntent
    data object ToggleFavorite : BusRouteIntent
}

sealed interface BusRouteSideEffect : UiSideEffect {
    data object NavigateBack : BusRouteSideEffect
    data class NavigateToBusNode(
        val nodeId: String,
        val nodeName: String,
        val nodeNo: String?,
        val cityCode: String,
    ) : BusRouteSideEffect
    data class ShowSnackbar(val added: Boolean) : BusRouteSideEffect
}
