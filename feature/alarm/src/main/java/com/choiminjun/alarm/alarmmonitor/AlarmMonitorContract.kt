package com.choiminjun.alarm.alarmmonitor

import com.choiminjun.base.UiIntent
import com.choiminjun.base.UiSideEffect
import com.choiminjun.base.UiState
import com.choiminjun.domain.model.alarm.AlarmInfo

data class AlarmMonitorState(
    val alarmInfo: AlarmInfo = AlarmInfo(),
    val routeNodeNames: List<String> = emptyList(),
    val isLoadingNodes: Boolean = true,
    val nearestNodeName: String? = null,
    val remainingStops: Int? = null,
) : UiState

sealed interface AlarmMonitorIntent : UiIntent {
    data object ClickBack : AlarmMonitorIntent
    data object StopAlarm : AlarmMonitorIntent
}

sealed interface AlarmMonitorSideEffect : UiSideEffect {
    data object NavigateBack : AlarmMonitorSideEffect
    data object AlarmStopped : AlarmMonitorSideEffect
    data object AlarmTriggered : AlarmMonitorSideEffect
}
