package com.choiminjun.domain.model.search

import com.choiminjun.domain.model.bus.BusNode
import com.choiminjun.domain.model.bus.BusRoute
import com.choiminjun.domain.model.bus.CityCode

data class RecentRouteSearch(
    val id: Long,
    val routeId: String,
    val routeNo: String,
    val routeType: String,
    val startNodeName: String,
    val endNodeName: String,
    val cityCode: CityCode,
)

fun RecentRouteSearch.toBusRoute() = BusRoute(
    routeId = routeId,
    routeNo = routeNo,
    routeType = routeType,
    startNodeName = startNodeName,
    endNodeName = endNodeName,
    cityCode = cityCode,
)

data class RecentNodeSearch(
    val id: Long,
    val nodeId: String,
    val nodeName: String,
    val nodeNo: String?,
    val cityCode: CityCode,
)

fun RecentNodeSearch.toBusNode() = BusNode(
    nodeId = nodeId,
    nodeName = nodeName,
    nodeNo = nodeNo,
    cityCode = cityCode,
)
