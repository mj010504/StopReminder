package com.choiminjun.network.model

import com.choiminjun.network.serializer.AnyToStringSerializer
import com.choiminjun.network.serializer.EmptyStringOrObjectSerializer
import com.choiminjun.network.serializer.SingleOrListSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetNodesByRouteResponse(
    @Serializable(with = GetNodesByRouteItemsSerializer::class)
    val items: GetNodesByRouteItems? = null,
    val numOfRows: Int,
    val pageNo: Int,
    val totalCount: Int,
)

@Serializable
data class GetNodesByRouteItems(
    @Serializable(with = GetNodesByRouteItemListSerializer::class)
    val item: List<GetNodesByRouteItem> = emptyList(),
)

@Serializable
data class GetNodesByRouteItem(
    @SerialName("routeid") val routeId: String,
    @SerialName("nodeid") val nodeId: String,
    @SerialName("nodenm") val nodeName: String,
    @Serializable(with = AnyToStringSerializer::class)
    @SerialName("nodeord") val nodeOrd: String,
    @SerialName("gpslati") val gpsLati: Double,
    @SerialName("gpslong") val gpsLong: Double,
    @Serializable(with = AnyToStringSerializer::class)
    @SerialName("updowncd") val upDownCd: String,
)

private object GetNodesByRouteItemsSerializer :
    EmptyStringOrObjectSerializer<GetNodesByRouteItems>(GetNodesByRouteItems.serializer())

private object GetNodesByRouteItemListSerializer :
    SingleOrListSerializer<GetNodesByRouteItem>(GetNodesByRouteItem.serializer())
