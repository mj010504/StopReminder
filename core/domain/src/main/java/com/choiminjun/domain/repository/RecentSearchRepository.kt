package com.choiminjun.domain.repository

import com.choiminjun.domain.model.bus.BusNode
import com.choiminjun.domain.model.bus.BusRoute
import com.choiminjun.domain.model.search.RecentNodeSearch
import com.choiminjun.domain.model.search.RecentRouteSearch
import kotlinx.coroutines.flow.Flow

interface RecentSearchRepository {
    fun getRecentRouteSearches(): Flow<List<RecentRouteSearch>>
    suspend fun saveRoute(route: BusRoute)
    suspend fun deleteRoute(routeId: String)
    fun getRecentNodeSearches(): Flow<List<RecentNodeSearch>>
    suspend fun saveNode(node: BusNode)
    suspend fun deleteNode(nodeId: String)
}
