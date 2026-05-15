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
import kotlinx.coroutines.Job
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
    private var toggleFavoriteJob: Job? = null

    init {
        val node = savedStateHandle.toRoute<HomeGraph.BusNodeRoute>()
        initNode(node)
        loadRoutes(node.nodeId)
        observeFavorite(node.nodeId)
    }

    private fun initNode(node: HomeGraph.BusNodeRoute) {
        reduce {
            copy(
                nodeId = node.nodeId,
                nodeName = node.nodeName,
                nodeNo = node.nodeNo,
                cityCode = node.cityCode,
            )
        }
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

    private fun toggleFavorite() {
        toggleFavoriteJob?.cancel()
        toggleFavoriteJob = viewModelScope.launch {
            val willAdd = !state.value.isFavorite
            suspendRunCatching {
                favoriteRepository.toggleFavoriteNode(
                    BusNode(
                        nodeId = state.value.nodeId,
                        nodeName = state.value.nodeName,
                        nodeNo = state.value.nodeNo,
                        cityCode = CityCode.valueOf(state.value.cityCode),
                    ),
                )
            }.onSuccess {
                postSideEffect(BusNodeSideEffect.ShowSnackbar(added = willAdd))
            }
        }
    }
}
