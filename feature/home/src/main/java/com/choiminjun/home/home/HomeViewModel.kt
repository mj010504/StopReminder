package com.choiminjun.home.home

import androidx.lifecycle.viewModelScope
import com.choiminjun.base.BaseViewModel
import com.choiminjun.common.util.suspendRunCatching
import com.choiminjun.domain.model.bus.BusNode
import com.choiminjun.domain.model.bus.BusRoute
import com.choiminjun.domain.model.bus.CityCode
import com.choiminjun.domain.repository.AlarmRepository
import com.choiminjun.domain.repository.BusRepository
import com.choiminjun.domain.repository.FavoriteRepository
import com.choiminjun.domain.repository.RecentSearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val busRepository: BusRepository,
    private val recentSearchRepository: RecentSearchRepository,
    private val alarmRepository: AlarmRepository,
    private val favoriteRepository: FavoriteRepository,
) : BaseViewModel<HomeState, HomeIntent, HomeSideEffect>(initialState = HomeState()) {

    private var searchJob: Job? = null

    init {
        loadRecentSearches()
        observeAlarm()
        observeFavorites()
    }

    private fun observeAlarm() {
        viewModelScope.launch {
            alarmRepository.observeAlarm().collect { alarmInfo ->
                if (alarmInfo.isTriggered) {
                    postSideEffect(HomeSideEffect.NavigateToAlarmRing)
                    return@collect
                }

                reduce { copy(alarmInfo = alarmInfo.takeIf { it.routeId.isNotBlank() }) }
            }
        }
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            favoriteRepository.getFavoriteRoutes().collect { routes ->
                reduce { copy(favoriteRoutes = routes) }
            }
        }

        viewModelScope.launch {
            favoriteRepository.getFavoriteNodes().collect { nodes ->
                reduce { copy(favoriteNodes = nodes) }
            }
        }
    }

    override suspend fun handleIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.ClickBack -> onBackClick()
            HomeIntent.FocusSearch -> focusSearch()
            is HomeIntent.ClickBusNode -> clickBusNode(intent.busNode)
            is HomeIntent.ClickBusRoute -> clickBusRoute(intent.busRoute)
            is HomeIntent.UpdateQuery -> updateQuery(intent.query)
            HomeIntent.ClearQuery -> clearQuery()
            is HomeIntent.SelectTab -> selectTab(intent)
            is HomeIntent.DeleteRecentRouteSearch -> deleteRecentRouteSearch(intent.id)
            is HomeIntent.DeleteRecentNodeSearch -> deleteRecentNodeSearch(intent.id)
            HomeIntent.ClickAlarmBanner -> postSideEffect(HomeSideEffect.NavigateToAlarmMonitor)
            is HomeIntent.ClickFavoriteRoute -> clickBusRoute(intent.busRoute)
            is HomeIntent.ClickFavoriteNode -> clickBusNode(intent.busNode)
        }
    }

    private fun clickBusRoute(busRoute: BusRoute) {
        saveRecentRoute(busRoute)
        postSideEffect(HomeSideEffect.NavigateToBusRoute(busRoute))
    }

    private fun clickBusNode(busNode: BusNode) {
        saveRecentNode(busNode)
        postSideEffect(HomeSideEffect.NavigateToBusNode(busNode))
    }

    private fun selectTab(intent: HomeIntent.SelectTab) {
        reduce { copy(selectedTab = intent.tab) }
    }

    private fun focusSearch() {
        reduce { copy(isSearching = true) }
    }

    private fun onBackClick() {
        searchJob?.cancel()
        reduce {
            copy(
                isSearching = false,
                searchQuery = "",
                searchedRoutes = emptyList(),
                searchedNodes = emptyList(),
                isLoading = false,
                selectedTab = SearchTab.BUS,
            )
        }
    }

    private fun clearQuery() {
        searchJob?.cancel()
        reduce {
            copy(
                searchQuery = "",
                searchedRoutes = emptyList(),
                searchedNodes = emptyList(),
                isLoading = false,
            )
        }
    }

    private fun updateQuery(query: String) {
        reduce { copy(searchQuery = query) }
        if (query.isNotBlank()) {
            search(query)
        } else {
            searchJob?.cancel()
            reduce {
                copy(
                    isLoading = false,
                    searchedRoutes = emptyList(),
                    searchedNodes = emptyList(),
                )
            }
        }
    }

    private fun search(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            reduce { copy(isLoading = true, searchedRoutes = emptyList(), searchedNodes = emptyList()) }

            val routesJob = launch {
                val result = suspendRunCatching { busRepository.getRouteNumbers(CityCode.BUSAN, query) }
                reduce { copy(searchedRoutes = result.getOrElse { emptyList() }) }
            }

            val nodesJob = launch {
                val result = suspendRunCatching { busRepository.getNodeNumbers(CityCode.BUSAN, query) }
                reduce { copy(searchedNodes = result.getOrElse { emptyList() }) }
            }

            routesJob.join()
            nodesJob.join()
            reduce { copy(isLoading = false) }
        }
    }

    private fun saveRecentRoute(busRoute: BusRoute) = viewModelScope.launch {
        suspendRunCatching {
            recentSearchRepository.saveRoute(busRoute)
        }
    }

    private fun saveRecentNode(busNode: BusNode) = viewModelScope.launch {
        suspendRunCatching {
            recentSearchRepository.saveNode(busNode)
        }
    }

    private fun deleteRecentRouteSearch(id: Long) =
        viewModelScope.launch {
            suspendRunCatching { recentSearchRepository.deleteRoute(id) }
        }

    private fun deleteRecentNodeSearch(id: Long) =
        viewModelScope.launch {
            suspendRunCatching { recentSearchRepository.deleteNode(id) }
        }

    private fun loadRecentSearches() {
        viewModelScope.launch {
            recentSearchRepository.getRecentRouteSearches().collect { searches ->
                reduce { copy(recentRouteSearches = searches) }
            }
        }

        viewModelScope.launch {
            recentSearchRepository.getRecentNodeSearches().collect { searches ->
                reduce { copy(recentNodeSearches = searches) }
            }
        }
    }
}
