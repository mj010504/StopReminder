package com.choiminjun.home.home

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.choiminjun.base.BaseViewModel
import com.choiminjun.common.util.suspendRunCatching
import com.choiminjun.domain.model.bus.CityCode
import com.choiminjun.domain.repository.BusRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val busRepository: BusRepository,
) : BaseViewModel<HomeState, HomeIntent, HomeSideEffect>(initialState = HomeState()) {

    private var searchJob: Job? = null

    override suspend fun handleIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.ClickBack -> clickBack()
            HomeIntent.FocusSearch -> focusSearch()
            is HomeIntent.ClickBusNode -> postSideEffect(HomeSideEffect.NavigateToBusNode(intent.nodeId))
            is HomeIntent.ClickBusRoute -> postSideEffect(HomeSideEffect.NavigateToBusRoute(intent.routeId))
            is HomeIntent.UpdateQuery -> updateQuery(intent.query)
        }
    }

    private fun focusSearch() {
        reduce { copy(isSearching = true) }
        loadRecentSearches()
    }

    private fun clickBack() {
        searchJob?.cancel()
        reduce {
            copy(
                isSearching = false,
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
                val result = suspendRunCatching { busRepository.getRouteNumbers(CityCode.BUSAN.code, query) }
                if (result.isFailure) Log.e("SearchError", "노선 실패: ${result.exceptionOrNull()?.message}")
                reduce { copy(searchedRoutes = result.getOrElse { emptyList() }) }
            }
            val nodesJob = launch {
                val result = suspendRunCatching { busRepository.getNodeNumbers(CityCode.BUSAN.code, query) }
                if (result.isFailure) Log.e("SearchError", "정류장 실패: ${result.exceptionOrNull()?.message}")
                reduce { copy(searchedNodes = result.getOrElse { emptyList() }) }
            }

            routesJob.join()
            nodesJob.join()
            reduce { copy(isLoading = false) }
        }
    }

    private fun loadRecentSearches() {
        // TODO: 최근 검색어 불러오기
    }
}
