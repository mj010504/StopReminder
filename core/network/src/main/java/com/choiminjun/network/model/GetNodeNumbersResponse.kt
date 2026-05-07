package com.choiminjun.network.model

import com.choiminjun.network.serializer.EmptyStringOrObjectSerializer
import com.choiminjun.network.serializer.NullableAnyToStringSerializer
import com.choiminjun.network.serializer.SingleOrListSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetNodeNumbersResponse(
    @Serializable(with = GetNodeNumbersItemsSerializer::class)
    val items: GetNodeNumbersItems? = null,
    val numOfRows: Int,
    val pageNo: Int,
    val totalCount: Int,
)

@Serializable
data class GetNodeNumbersItems(
    @Serializable(with = GetNodeNumbersItemListSerializer::class)
    val item: List<GetNodeNumbersItem> = emptyList(),
)

@Serializable
data class GetNodeNumbersItem(
    @SerialName("gpslati") val gpsLati: Double,
    @SerialName("gpslong") val gpsLong: Double,
    @SerialName("nodeid") val nodeId: String,
    @SerialName("nodenm") val nodeName: String,
    @Serializable(with = NullableAnyToStringSerializer::class)
    @SerialName("nodeno") val nodeNo: String? = null,
)

private object GetNodeNumbersItemsSerializer :
    EmptyStringOrObjectSerializer<GetNodeNumbersItems>(GetNodeNumbersItems.serializer())

private object GetNodeNumbersItemListSerializer :
    SingleOrListSerializer<GetNodeNumbersItem>(GetNodeNumbersItem.serializer())
