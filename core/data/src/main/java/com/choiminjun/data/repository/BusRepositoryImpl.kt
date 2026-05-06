package com.choiminjun.data.repository

import com.choiminjun.domain.model.bus.BusNode
import com.choiminjun.domain.model.bus.BusRoute
import com.choiminjun.domain.repository.BusRepository
import com.choiminjun.network.source.BusDataSource
import javax.inject.Inject

class BusRepositoryImpl @Inject constructor(
    private val busDataSource: BusDataSource,
) : BusRepository {

    override suspend fun getRouteNumbers(cityCode: Int, routeNo: String): List<BusRoute> =
        busDataSource.getRouteNumbers(cityCode, routeNo).map {
            BusRoute(
                routeId = it.routeId,
                routeNo = it.routeNo,
                routeType = "",
                startNodeName = it.startNodeName,
                endNodeName = it.endNodeName,
            )
        }

    override suspend fun getNodesByRoute(cityCode: Int, routeId: String): List<BusNode> =
        busDataSource.getNodesByRoute(cityCode, routeId).map {
            BusNode(
                nodeId = it.nodeId,
                nodeName = it.nodeName,
                latitude = it.gpsLati,
                longitude = it.gpsLong,
            )
        }

    override suspend fun getNodeNumbers(cityCode: Int, nodeName: String): List<BusNode> =
        busDataSource.getNodeNumbers(cityCode, nodeName).map {
            BusNode(
                nodeId = it.nodeId,
                nodeName = it.nodeName,
                latitude = it.gpsLati,
                longitude = it.gpsLong,
            )
        }

    override suspend fun getRoutesByNode(cityCode: Int, nodeId: String): List<BusRoute> =
        busDataSource.getRoutesByNode(cityCode, nodeId).map {
            BusRoute(
                routeId = it.routeId,
                routeNo = it.routeNo,
                routeType = it.routeType,
                startNodeName = it.startNodeName,
                endNodeName = it.endNodeName,
            )
        }
}
