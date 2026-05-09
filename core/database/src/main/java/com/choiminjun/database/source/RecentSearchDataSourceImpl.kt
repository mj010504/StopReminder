package com.choiminjun.database.source

import com.choiminjun.database.dao.RecentSearchDao
import com.choiminjun.database.model.RecentNodeEntity
import com.choiminjun.database.model.RecentRouteEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RecentSearchDataSourceImpl @Inject constructor(
    private val dao: RecentSearchDao,
) : RecentSearchDataSource {
    override fun getRecentRoutes(): Flow<List<RecentRouteEntity>> = dao.getAllRoutes()

    override suspend fun saveRoute(entity: RecentRouteEntity) {
        dao.deleteNode(entity.id)
        dao.insertRoute(entity)
    }

    override suspend fun deleteRoute(id: Long) = dao.deleteRoute(id)

    override fun getRecentNodes(): Flow<List<RecentNodeEntity>> = dao.getAllNodes()

    override suspend fun saveNode(entity: RecentNodeEntity) {
        dao.deleteNode(entity.id)
        dao.insertNode(entity)
    }

    override suspend fun deleteNode(id: Long) = dao.deleteNode(id)
}
