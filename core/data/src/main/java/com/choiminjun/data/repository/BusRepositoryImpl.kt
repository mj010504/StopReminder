package com.choiminjun.data.repository

import com.choiminjun.domain.model.bus.BusNode
import com.choiminjun.domain.model.bus.BusRoute
import com.choiminjun.domain.model.bus.CityCode
import com.choiminjun.domain.repository.BusRepository
import com.choiminjun.network.source.BusDataSource
import javax.inject.Inject

class BusRepositoryImpl @Inject constructor(
    private val busDataSource: BusDataSource,
) : BusRepository {

    override suspend fun getRouteNumbers(cityCode: CityCode, routeNo: String): List<BusRoute> =
        busDataSource.getRouteNumbers(cityCode.code, routeNo).map {
            BusRoute(
                routeId = it.routeId,
                routeNo = it.routeNo,
                routeType = it.routeType.removeSuffix("버스"),
                startNodeName = it.startNodeName,
                endNodeName = it.endNodeName,
                cityCode = cityCode,
            )
        }

    override suspend fun getNodesByRoute(cityCode: CityCode, routeId: String): List<BusNode> =
        busDataSource.getNodesByRoute(cityCode.code, routeId).map {
            BusNode(
                nodeId = it.nodeId,
                nodeName = it.nodeName,
                nodeNo = it.nodeNo,
                latitude = it.gpsLati,
                longitude = it.gpsLong,
                cityCode = cityCode,
            )
        }

    override suspend fun getNodeNumbers(cityCode: CityCode, nodeName: String): List<BusNode> =
        busDataSource.getNodeNumbers(cityCode.code, nodeName).map {
            BusNode(
                nodeId = it.nodeId,
                nodeName = it.nodeName,
                nodeNo = it.nodeNo,
                latitude = it.gpsLati,
                longitude = it.gpsLong,
                cityCode = cityCode,
            )
        }

    override suspend fun getRoutesByNode(cityCode: CityCode, nodeId: String): List<BusRoute> =
        busDataSource.getRoutesByNode(cityCode.code, nodeId).map {
            BusRoute(
                routeId = it.routeId,
                routeNo = it.routeNo,
                routeType = it.routeType.removeSuffix("버스"),
                startNodeName = it.startNodeName,
                endNodeName = it.endNodeName,
                cityCode = cityCode,
            )
        }
}
