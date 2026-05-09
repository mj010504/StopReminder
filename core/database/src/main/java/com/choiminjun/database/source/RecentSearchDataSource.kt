package com.choiminjun.database.source

import com.choiminjun.database.model.RecentNodeEntity
import com.choiminjun.database.model.RecentRouteEntity
import kotlinx.coroutines.flow.Flow

interface RecentSearchDataSource {
    fun getRecentRoutes(): Flow<List<RecentRouteEntity>>
    suspend fun saveRoute(entity: RecentRouteEntity)
    suspend fun deleteRoute(routeId: String)
    fun getRecentNodes(): Flow<List<RecentNodeEntity>>
    suspend fun saveNode(entity: RecentNodeEntity)
    suspend fun deleteNode(nodeId: String)
}
