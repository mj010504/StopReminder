package com.choiminjun.network.model

import com.choiminjun.network.serializer.AnyToStringSerializer
import com.choiminjun.network.serializer.EmptyStringOrObjectSerializer
import com.choiminjun.network.serializer.SingleOrListSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetRouteNumbersResponse(
    @Serializable(with = GetRouteNumbersItemsSerializer::class)
    val items: GetRouteNumbersItems? = null,
    val numOfRows: Int,
    val pageNo: Int,
    val totalCount: Int,
)

@Serializable
data class GetRouteNumbersItems(
    @Serializable(with = GetRouteNumbersItemListSerializer::class)
    val item: List<GetRouteNumbersItem> = emptyList(),
)

@Serializable
data class GetRouteNumbersItem(
    @SerialName("routeid") val routeId: String,
    @Serializable(with = AnyToStringSerializer::class)
    @SerialName("routeno") val routeNo: String,
    @SerialName("routetp") val routeType: String,
    @SerialName("endnodenm") val endNodeName: String,
    @SerialName("startnodenm") val startNodeName: String,
)

private object GetRouteNumbersItemsSerializer :
    EmptyStringOrObjectSerializer<GetRouteNumbersItems>(GetRouteNumbersItems.serializer())

private object GetRouteNumbersItemListSerializer :
    SingleOrListSerializer<GetRouteNumbersItem>(GetRouteNumbersItem.serializer())
