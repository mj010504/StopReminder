package com.choiminjun.home.route

import com.choiminjun.base.UiIntent
import com.choiminjun.base.UiSideEffect
import com.choiminjun.base.UiState
import com.choiminjun.domain.model.bus.BusNode
import com.choiminjun.domain.model.bus.BusRoute

data class BusRouteState(
    val busRoute: BusRoute? = null,
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
    data class NavigateToBusNode(val busNode: BusNode) : BusRouteSideEffect
    data class ShowSnackbar(val added: Boolean) : BusRouteSideEffect
}
