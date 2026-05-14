package com.choiminjun.home.alarmsetting

import com.choiminjun.base.UiIntent
import com.choiminjun.base.UiSideEffect
import com.choiminjun.base.UiState
import com.choiminjun.domain.model.bus.BusNode

data class AlarmSettingState(
    val routeNo: String = "",
    val routeId: String = "",
    val isLoading: Boolean = false,
    val nodes: List<BusNode> = emptyList(),
    val selectedNode: BusNode? = null,
    val selectedStopsBefore: Int = 1,
) : UiState

sealed interface AlarmSettingIntent : UiIntent {
    data object ClickBack : AlarmSettingIntent
    data class SelectNode(val node: BusNode) : AlarmSettingIntent
    data object DismissBottomSheet : AlarmSettingIntent
    data object ConfirmAlarm : AlarmSettingIntent
    data class SelectStopsBefore(val stops: Int) : AlarmSettingIntent
}

sealed interface AlarmSettingSideEffect : UiSideEffect {
    data object NavigateBack : AlarmSettingSideEffect
    data class AlarmConfirmed(
        val routeNo: String,
        val nodeName: String,
    ) : AlarmSettingSideEffect
}
