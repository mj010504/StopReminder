package com.choiminjun.domain.model.bus

data class BusNode(
    val nodeId: String,
    val nodeName: String,
    val nodeNo: String? = null,
    val latitude: Double,
    val longitude: Double,
    val cityCode: CityCode,
)
