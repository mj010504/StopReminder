package com.choiminjun.network.model

import kotlinx.serialization.Serializable

@Serializable
data class TagoBaseResponse<T>(
    val response: TagoResponse<T>,
)

@Serializable
data class TagoResponse<T>(
    val header: TagoHeader,
    val body: T,
)

@Serializable
data class TagoHeader(
    val resultCode: String,
    val resultMsg: String,
)

fun <T> TagoBaseResponse<T>.getData(): T {
    return response.body ?: error("Response data is null. code=${response.header.resultCode}, message=${response.header.resultMsg}")
}
