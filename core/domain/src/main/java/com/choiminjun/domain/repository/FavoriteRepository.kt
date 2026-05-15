package com.choiminjun.domain.repository

import com.choiminjun.domain.model.bus.BusNode
import com.choiminjun.domain.model.bus.BusRoute
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    fun getFavoriteRoutes(): Flow<List<BusRoute>>
    fun getFavoriteNodes(): Flow<List<BusNode>>
    suspend fun toggleFavoriteRoute(route: BusRoute)
    suspend fun toggleFavoriteNode(node: BusNode)
    fun isFavoriteRoute(routeId: String): Flow<Boolean>
    fun isFavoriteNode(nodeId: String): Flow<Boolean>
}
