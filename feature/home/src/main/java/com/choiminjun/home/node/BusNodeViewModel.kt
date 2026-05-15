package com.choiminjun.home.node

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.choiminjun.base.BaseViewModel
import com.choiminjun.common.util.suspendRunCatching
import com.choiminjun.domain.model.bus.BusNode
import com.choiminjun.domain.model.bus.BusRoute
import com.choiminjun.domain.model.bus.CityCode
import com.choiminjun.domain.repository.BusRepository
import com.choiminjun.domain.repository.FavoriteRepository
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
    private val favoriteRepository: FavoriteRepository,
) : BaseViewModel<BusNodeState, BusNodeIntent, BusNodeSideEffect>(
    initialState = BusNodeState(),
) {
    private lateinit var currentNode: HomeGraph.BusNodeRoute

    init {
        initNode(savedStateHandle)
        loadRoutes(currentNode.nodeId)
        observeFavorite(currentNode.nodeId)
    }

    private fun initNode(savedStateHandle: SavedStateHandle) {
        currentNode = savedStateHandle.toRoute<HomeGraph.BusNodeRoute>()
        reduce { copy(nodeName = currentNode.nodeName, nodeNo = currentNode.nodeNo) }
    }

    override suspend fun handleIntent(intent: BusNodeIntent) {
        when (intent) {
            BusNodeIntent.ClickBack -> postSideEffect(BusNodeSideEffect.NavigateBack)
            is BusNodeIntent.ClickAlarm -> postSideEffect(BusNodeSideEffect.NavigateToAlarm(intent.routeId, intent.routeNo))
            is BusNodeIntent.ClickBusRoute -> clickBusRoute(intent.route)
            BusNodeIntent.ToggleFavorite -> toggleFavorite()
        }
    }

    private fun clickBusRoute(busRoute: BusRoute) {
        saveRecentRoute(busRoute)
        postSideEffect(
            BusNodeSideEffect.NavigateToBusRoute(
                busRoute.routeId,
                busRoute.routeNo,
                busRoute.routeType,
                busRoute.startNodeName,
                busRoute.endNodeName,
                busRoute.cityCode.name,
            ),
        )
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

    private fun observeFavorite(nodeId: String) {
        viewModelScope.launch {
            favoriteRepository.isFavoriteNode(nodeId).collect { isFavorite ->
                reduce { copy(isFavorite = isFavorite) }
            }
        }
    }

    private fun toggleFavorite() = viewModelScope.launch {
        val willAdd = !state.value.isFavorite
        suspendRunCatching {
            favoriteRepository.toggleFavoriteNode(
                BusNode(
                    nodeId = currentNode.nodeId,
                    nodeName = currentNode.nodeName,
                    nodeNo = currentNode.nodeNo,
                    cityCode = CityCode.valueOf(currentNode.cityCode),
                ),
            )
        }.onSuccess {
            postSideEffect(BusNodeSideEffect.ShowSnackbar(added = willAdd))
        }
    }
}
