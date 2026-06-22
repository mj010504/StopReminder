package com.choiminjun.home.node

import com.choiminjun.base.UiIntent
import com.choiminjun.base.UiSideEffect
import com.choiminjun.base.UiState
import com.choiminjun.domain.model.bus.BusNode
import com.choiminjun.domain.model.bus.BusRoute

data class BusNodeState(
    val busNode: BusNode? = null,
    val isLoading: Boolean = false,
    val routes: List<BusRoute> = emptyList(),
    val isFavorite: Boolean = false,
) : UiState

sealed interface BusNodeIntent : UiIntent {
    data object ClickBack : BusNodeIntent
    data class ClickAlarm(val route: BusRoute) : BusNodeIntent
    data class ClickBusRoute(val route: BusRoute) : BusNodeIntent
    data object ToggleFavorite : BusNodeIntent
}

sealed interface BusNodeSideEffect : UiSideEffect {
    data object NavigateBack : BusNodeSideEffect
    data class NavigateToAlarm(
        val routeId: String,
        val routeNo: String,
        val boardingNodeId: String,
        val boardingNodeName: String,
    ) : BusNodeSideEffect
    data class NavigateToBusRoute(val busRoute: BusRoute) : BusNodeSideEffect
    data class ShowSnackbar(val added: Boolean) : BusNodeSideEffect
}
