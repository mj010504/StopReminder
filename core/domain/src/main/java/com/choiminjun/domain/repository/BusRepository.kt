package com.choiminjun.domain.repository

import com.choiminjun.domain.model.bus.BusNode
import com.choiminjun.domain.model.bus.BusRoute

interface BusRepository {
    suspend fun getRouteNumbers(cityCode: Int, routeNo: String): List<BusRoute>
    suspend fun getNodesByRoute(cityCode: Int, routeId: String): List<BusNode>
    suspend fun getNodeNumbers(cityCode: Int, nodeName: String): List<BusNode>
    suspend fun getRoutesByNode(cityCode: Int, nodeId: String): List<BusRoute>
}
