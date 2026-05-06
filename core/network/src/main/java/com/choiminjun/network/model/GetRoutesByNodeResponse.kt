package com.choiminjun.network.model

import com.choiminjun.network.serializer.AnyToStringSerializer
import com.choiminjun.network.serializer.EmptyStringOrObjectSerializer
import com.choiminjun.network.serializer.SingleOrListSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetRoutesByNodeResponse(
    @Serializable(with = GetRoutesByNodeItemsSerializer::class)
    val items: GetRoutesByNodeItems? = null,
    val numOfRows: Int,
    val pageNo: Int,
)

@Serializable
data class GetRoutesByNodeItems(
    @Serializable(with = GetRoutesByNodeItemListSerializer::class)
    val item: List<GetRoutesByNodeItem> = emptyList(),
)

@Serializable
data class GetRoutesByNodeItem(
    @SerialName("routeid") val routeId: String,
    @Serializable(with = AnyToStringSerializer::class)
    @SerialName("routeno") val routeNo: String,
    @SerialName("routetp") val routeType: String,
    @SerialName("endnodenm") val endNodeName: String,
    @SerialName("startnodenm") val startNodeName: String,
)

private object GetRoutesByNodeItemsSerializer :
    EmptyStringOrObjectSerializer<GetRoutesByNodeItems>(GetRoutesByNodeItems.serializer())

private object GetRoutesByNodeItemListSerializer :
    SingleOrListSerializer<GetRoutesByNodeItem>(GetRoutesByNodeItem.serializer())
