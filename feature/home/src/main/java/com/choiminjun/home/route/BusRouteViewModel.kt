package com.choiminjun.home.route

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
class BusRouteViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val busRepository: BusRepository,
    private val recentSearchRepository: RecentSearchRepository,
    private val favoriteRepository: FavoriteRepository,
) : BaseViewModel<BusRouteState, BusRouteIntent, BusRouteSideEffect>(
    initialState = BusRouteState(),
) {
    private var toggleFavoriteJob: Job? = null

    init {
        val route = savedStateHandle.toRoute<HomeGraph.BusRouteRoute>()
        initRoute(route)
        loadNodes(route.routeId)
        observeFavorite(route.routeId)
    }

    private fun initRoute(route: HomeGraph.BusRouteRoute) {
        reduce {
            copy(
                routeId = route.routeId,
                routeNo = route.routeNo,
                routeType = route.routeType,
                startNodeName = route.startNodeName,
                endNodeName = route.endNodeName,
                cityCode = route.cityCode,
            )
        }
    }

    override suspend fun handleIntent(intent: BusRouteIntent) {
        when (intent) {
            BusRouteIntent.ClickBack -> postSideEffect(BusRouteSideEffect.NavigateBack)
            is BusRouteIntent.ClickBusNode -> clickBusNode(intent.busNode)
            BusRouteIntent.ToggleFavorite -> toggleFavorite()
        }
    }

    private fun clickBusNode(busNode: BusNode) {
        saveRecentNode(busNode)
        postSideEffect(
            BusRouteSideEffect.NavigateToBusNode(
                busNode.nodeId,
                busNode.nodeName,
                busNode.nodeNo,
                busNode.cityCode.name,
            ),
        )
    }

    private fun saveRecentNode(busNode: BusNode) = viewModelScope.launch {
        suspendRunCatching {
            recentSearchRepository.saveNode(busNode)
        }
    }

    private fun observeFavorite(routeId: String) {
        viewModelScope.launch {
            favoriteRepository.isFavoriteRoute(routeId).collect { isFavorite ->
                reduce { copy(isFavorite = isFavorite) }
            }
        }
    }

    private fun toggleFavorite() {
        toggleFavoriteJob?.cancel()
        toggleFavoriteJob = viewModelScope.launch {
            val willAdd = !state.value.isFavorite
            suspendRunCatching {
                favoriteRepository.toggleFavoriteRoute(
                    BusRoute(
                        routeId = state.value.routeId,
                        routeNo = state.value.routeNo,
                        routeType = state.value.routeType,
                        startNodeName = state.value.startNodeName,
                        endNodeName = state.value.endNodeName,
                        cityCode = CityCode.valueOf(state.value.cityCode),
                    ),
                )
            }.onSuccess {
                postSideEffect(BusRouteSideEffect.ShowSnackbar(added = willAdd))
            }
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
