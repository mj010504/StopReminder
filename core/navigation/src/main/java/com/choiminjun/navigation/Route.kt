package com.choiminjun.navigation

import kotlinx.serialization.Serializable

sealed interface Route

@Serializable
data object HomeBaseRoute : Route

sealed interface HomeGraph : Route {
    @Serializable
    data object HomeRoute : HomeGraph

    @Serializable
    data class BusRouteRoute(
        val routeId: String,
        val routeNo: String,
        val routeType: String = "",
        val startNodeName: String = "",
        val endNodeName: String = "",
        val cityCode: String = "BUSAN",
    ) : HomeGraph

    @Serializable
    data class BusNodeRoute(
        val nodeId: String,
        val nodeName: String,
        val nodeNo: String? = null,
        val cityCode: String = "BUSAN",
    ) : HomeGraph

    @Serializable
    data class AlarmSettingRoute(
        val routeId: String,
        val routeNo: String,
        val boardingNodeId: String = "",
        val boardingNodeName: String = "",
    ) : HomeGraph

    @Serializable
    data object AlarmMonitorRoute : HomeGraph

    @Serializable
    data object AlarmRingRoute : HomeGraph
}
