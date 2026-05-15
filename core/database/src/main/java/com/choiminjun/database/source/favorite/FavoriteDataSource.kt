package com.choiminjun.database.source.favorite

import com.choiminjun.database.model.FavoriteNodeEntity
import com.choiminjun.database.model.FavoriteRouteEntity
import kotlinx.coroutines.flow.Flow

interface FavoriteDataSource {
    fun getFavoriteRoutes(): Flow<List<FavoriteRouteEntity>>
    suspend fun saveFavoriteRoute(entity: FavoriteRouteEntity)
    suspend fun deleteFavoriteRoute(routeId: String)
    fun isFavoriteRoute(routeId: String): Flow<Boolean>

    fun getFavoriteNodes(): Flow<List<FavoriteNodeEntity>>
    suspend fun saveFavoriteNode(entity: FavoriteNodeEntity)
    suspend fun deleteFavoriteNode(nodeId: String)
    fun isFavoriteNode(nodeId: String): Flow<Boolean>
}
