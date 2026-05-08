package com.choiminjun.home.route

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.choiminjun.base.BaseViewModel
import com.choiminjun.common.util.suspendRunCatching
import com.choiminjun.domain.model.bus.CityCode
import com.choiminjun.domain.repository.BusRepository
import com.choiminjun.navigation.HomeGraph
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BusRouteViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val busRepository: BusRepository,
) : BaseViewModel<BusRouteState, BusRouteIntent, BusRouteSideEffect>(
    initialState = BusRouteState(),
) {
    init {
        val route = savedStateHandle.toRoute<HomeGraph.BusRouteRoute>()
        reduce { copy(routeNo = route.routeNo) }
        loadNodes(route.routeId)
    }

    override suspend fun handleIntent(intent: BusRouteIntent) {
        when (intent) {
            BusRouteIntent.ClickBack -> postSideEffect(BusRouteSideEffect.NavigateBack)
            is BusRouteIntent.ClickBusNode -> postSideEffect(BusRouteSideEffect.NavigateToBusNode(intent.nodeId, intent.nodeName, intent.nodeNo))
        }
    }

    private fun loadNodes(routeId: String) {
        viewModelScope.launch {
            reduce { copy(isLoading = true) }
            val nodes = suspendRunCatching { busRepository.getNodesByRoute(CityCode.BUSAN, routeId) }
                .getOrElse { emptyList() }
            reduce { copy(isLoading = false, nodes = nodes) }
        }
    }
}
