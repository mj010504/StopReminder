package com.choiminjun.database.source.favorite

import com.choiminjun.database.dao.FavoriteDao
import com.choiminjun.database.model.FavoriteNodeEntity
import com.choiminjun.database.model.FavoriteRouteEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoriteDataSourceImpl @Inject constructor(
    private val dao: FavoriteDao,
) : FavoriteDataSource {
    override fun getFavoriteRoutes(): Flow<List<FavoriteRouteEntity>> = dao.getAllRoutes()
    override suspend fun saveFavoriteRoute(entity: FavoriteRouteEntity) = dao.insertRoute(entity)
    override suspend fun deleteFavoriteRoute(routeId: String) = dao.deleteRoute(routeId)
    override fun isFavoriteRoute(routeId: String): Flow<Boolean> = dao.isFavoriteRoute(routeId)

    override fun getFavoriteNodes(): Flow<List<FavoriteNodeEntity>> = dao.getAllNodes()
    override suspend fun saveFavoriteNode(entity: FavoriteNodeEntity) = dao.insertNode(entity)
    override suspend fun deleteFavoriteNode(nodeId: String) = dao.deleteNode(nodeId)
    override fun isFavoriteNode(nodeId: String): Flow<Boolean> = dao.isFavoriteNode(nodeId)
}
