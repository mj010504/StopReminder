package com.choiminjun.data.repository

import com.choiminjun.database.model.FavoriteNodeEntity
import com.choiminjun.database.model.FavoriteRouteEntity
import com.choiminjun.database.source.favorite.FavoriteDataSource
import com.choiminjun.domain.model.bus.BusNode
import com.choiminjun.domain.model.bus.BusRoute
import com.choiminjun.domain.model.bus.CityCode
import com.choiminjun.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val dataSource: FavoriteDataSource,
) : FavoriteRepository {
    private val toggleMutex = Mutex()

    override fun getFavoriteRoutes(): Flow<List<BusRoute>> =
        dataSource.getFavoriteRoutes().map { entities -> entities.map { it.toDomain() } }

    override fun getFavoriteNodes(): Flow<List<BusNode>> =
        dataSource.getFavoriteNodes().map { entities -> entities.map { it.toDomain() } }

    override suspend fun toggleFavoriteRoute(route: BusRoute) {
        toggleMutex.withLock {
            if (dataSource.isFavoriteRoute(route.routeId).first()) {
                dataSource.deleteFavoriteRoute(route.routeId)
            } else {
                dataSource.saveFavoriteRoute(route.toEntity())
            }
        }
    }

    override suspend fun toggleFavoriteNode(node: BusNode) {
        toggleMutex.withLock {
            if (dataSource.isFavoriteNode(node.nodeId).first()) {
                dataSource.deleteFavoriteNode(node.nodeId)
            } else {
                dataSource.saveFavoriteNode(node.toEntity())
            }
        }
    }

    override fun isFavoriteRoute(routeId: String): Flow<Boolean> =
        dataSource.isFavoriteRoute(routeId)

    override fun isFavoriteNode(nodeId: String): Flow<Boolean> =
        dataSource.isFavoriteNode(nodeId)

    private fun FavoriteRouteEntity.toDomain() = BusRoute(
        routeId = routeId,
        routeNo = routeNo,
        routeType = routeType,
        startNodeName = startNodeName,
        endNodeName = endNodeName,
        cityCode = CityCode.valueOf(cityCode),
    )

    private fun FavoriteNodeEntity.toDomain() = BusNode(
        nodeId = nodeId,
        nodeName = nodeName,
        nodeNo = nodeNo,
        latitude = latitude,
        longitude = longitude,
        cityCode = CityCode.valueOf(cityCode),
    )

    private fun BusRoute.toEntity() = FavoriteRouteEntity(
        routeId = routeId,
        routeNo = routeNo,
        routeType = routeType,
        startNodeName = startNodeName,
        endNodeName = endNodeName,
        cityCode = cityCode.name,
    )

    private fun BusNode.toEntity() = FavoriteNodeEntity(
        nodeId = nodeId,
        nodeName = nodeName,
        nodeNo = nodeNo,
        latitude = latitude,
        longitude = longitude,
        cityCode = cityCode.name,
    )
}
