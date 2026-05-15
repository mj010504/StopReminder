package com.choiminjun.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_route")
data class FavoriteRouteEntity(
    @PrimaryKey val routeId: String,
    val routeNo: String,
    val routeType: String,
    val startNodeName: String,
    val endNodeName: String,
    val cityCode: String,
)

@Entity(tableName = "favorite_node")
data class FavoriteNodeEntity(
    @PrimaryKey val nodeId: String,
    val nodeName: String,
    val nodeNo: String?,
    val latitude: Double?,
    val longitude: Double?,
    val cityCode: String,
)
