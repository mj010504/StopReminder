package com.choiminjun.domain.model.bus

data class BusRoute(
    val routeId: String,
    val routeNo: String,
    val routeType: String,
    val startNodeName: String,
    val endNodeName: String,
)
