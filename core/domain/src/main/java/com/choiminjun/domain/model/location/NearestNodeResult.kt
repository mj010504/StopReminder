package com.choiminjun.domain.model.location

data class NearestNodeResult(
    val nearestNodeIndex: Int,
    val destIndex: Int,
    val nearestNodeName: String,
    val distanceMeters: Double,
    val remaining: Int,
)
