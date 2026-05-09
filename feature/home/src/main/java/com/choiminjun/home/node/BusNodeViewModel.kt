package com.choiminjun.home.node

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.choiminjun.base.BaseViewModel
import com.choiminjun.common.util.suspendRunCatching
import com.choiminjun.domain.model.bus.BusRoute
import com.choiminjun.domain.model.bus.CityCode
import com.choiminjun.domain.repository.BusRepository
import com.choiminjun.domain.repository.RecentSearchRepository
import com.choiminjun.navigation.HomeGraph
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BusNodeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val busRepository: BusRepository,
    private val recentSearchRepository: RecentSearchRepository,
) : BaseViewModel<BusNodeState, BusNodeIntent, BusNodeSideEffect>(
    initialState = BusNodeState(),
) {
    init {
        val route = savedStateHandle.toRoute<HomeGraph.BusNodeRoute>()
        reduce { copy(nodeName = route.nodeName, nodeNo = route.nodeNo) }
        loadRoutes(route.nodeId)
    }

    override suspend fun handleIntent(intent: BusNodeIntent) {
        when (intent) {
            BusNodeIntent.ClickBack -> postSideEffect(BusNodeSideEffect.NavigateBack)
            is BusNodeIntent.ClickAlarm -> postSideEffect(BusNodeSideEffect.NavigateToAlarm(intent.routeId))
            is BusNodeIntent.ClickBusRoute -> clickBusRoute(intent.route)
        }
    }

    private fun clickBusRoute(busRoute: BusRoute) {
        saveRecentRoute(busRoute)
        postSideEffect(BusNodeSideEffect.NavigateToBusRoute(busRoute.routeId, busRoute.routeNo))
    }

    private fun saveRecentRoute(busRoute: BusRoute) = viewModelScope.launch {
        suspendRunCatching {
            recentSearchRepository.saveRoute(busRoute)
        }
    }

    private fun loadRoutes(nodeId: String) {
        viewModelScope.launch {
            reduce { copy(isLoading = true) }
            val routes = suspendRunCatching { busRepository.getRoutesByNode(CityCode.BUSAN, nodeId) }
                .getOrElse { emptyList() }
            reduce { copy(isLoading = false, routes = routes) }
        }
    }
}
