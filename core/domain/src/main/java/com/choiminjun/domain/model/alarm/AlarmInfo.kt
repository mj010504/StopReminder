package com.choiminjun.domain.model.alarm

data class AlarmInfo(
    val routeId: String = "",
    val routeNo: String = "",
    val destNodeId: String = "",
    val destNodeName: String = "",
    val stopsBeforeAlarm: Int = 1,
)
