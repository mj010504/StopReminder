package com.choiminjun.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recent_route")
data class RecentRouteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val routeId: String,
    val routeNo: String,
    val routeType: String,
    val startNodeName: String,
    val endNodeName: String,
    val cityCode: String,
)

@Entity(tableName = "recent_node")
data class RecentNodeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nodeId: String,
    val nodeName: String,
    val nodeNo: String?,
    val cityCode: String,
)
