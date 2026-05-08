package com.choiminjun.domain.repository

import com.choiminjun.domain.model.bus.BusNode
import com.choiminjun.domain.model.bus.BusRoute
import com.choiminjun.domain.model.bus.CityCode

interface BusRepository {
    suspend fun getRouteNumbers(cityCode: CityCode, routeNo: String): List<BusRoute>
    suspend fun getNodesByRoute(cityCode: CityCode, routeId: String): List<BusNode>
    suspend fun getNodeNumbers(cityCode: CityCode, nodeName: String): List<BusNode>
    suspend fun getRoutesByNode(cityCode: CityCode, nodeId: String): List<BusRoute>
}
