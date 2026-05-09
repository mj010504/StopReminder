package com.choiminjun.data.repository

import com.choiminjun.database.model.RecentNodeEntity
import com.choiminjun.database.model.RecentRouteEntity
import com.choiminjun.database.source.RecentSearchDataSource
import com.choiminjun.domain.model.bus.BusNode
import com.choiminjun.domain.model.bus.BusRoute
import com.choiminjun.domain.model.bus.CityCode
import com.choiminjun.domain.model.search.RecentNodeSearch
import com.choiminjun.domain.model.search.RecentRouteSearch
import com.choiminjun.domain.repository.RecentSearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RecentSearchRepositoryImpl @Inject constructor(
    private val dataSource: RecentSearchDataSource,
) : RecentSearchRepository {
    override fun getRecentRouteSearches(): Flow<List<RecentRouteSearch>> =
        dataSource.getRecentRoutes().map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun saveRoute(route: BusRoute) {
        dataSource.saveRoute(route.toEntity())
    }

    override suspend fun deleteRoute(id: Long) {
        dataSource.deleteRoute(id)
    }

    override fun getRecentNodeSearches(): Flow<List<RecentNodeSearch>> =
        dataSource.getRecentNodes().map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun saveNode(node: BusNode) {
        dataSource.saveNode(node.toEntity())
    }

    override suspend fun deleteNode(id: Long) {
        dataSource.deleteNode(id)
    }

    // --- Mapper ---
    private fun RecentRouteEntity.toDomain() = RecentRouteSearch(
        id = id,
        routeId = routeId,
        routeNo = routeNo,
        routeType = routeType,
        startNodeName = startNodeName,
        endNodeName = endNodeName,
        cityCode = CityCode.valueOf(cityCode),
    )

    private fun RecentNodeEntity.toDomain() = RecentNodeSearch(
        id = id,
        nodeId = nodeId,
        nodeName = nodeName,
        nodeNo = nodeNo,
        cityCode = CityCode.valueOf(cityCode),
    )

    private fun BusRoute.toEntity() = RecentRouteEntity(
        routeId = routeId,
        routeNo = routeNo,
        routeType = routeType,
        startNodeName = startNodeName,
        endNodeName = endNodeName,
        cityCode = cityCode.name,
    )

    private fun BusNode.toEntity() = RecentNodeEntity(
        nodeId = nodeId,
        nodeName = nodeName,
        nodeNo = nodeNo,
        cityCode = cityCode.name,
    )
}
