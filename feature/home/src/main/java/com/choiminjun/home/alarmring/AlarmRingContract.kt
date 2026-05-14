package com.choiminjun.home.alarmring

import com.choiminjun.base.UiIntent
import com.choiminjun.base.UiSideEffect
import com.choiminjun.base.UiState
import com.choiminjun.domain.model.alarm.AlarmInfo

data class AlarmRingState(
    val alarmInfo: AlarmInfo = AlarmInfo(),
) : UiState

sealed interface AlarmRingIntent : UiIntent {
    data object ConfirmDismiss : AlarmRingIntent
}

sealed interface AlarmRingSideEffect : UiSideEffect {
    data object NavigateToHome : AlarmRingSideEffect
}
