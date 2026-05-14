package com.choiminjun.home.alarmring

import androidx.lifecycle.viewModelScope
import com.choiminjun.base.BaseViewModel
import com.choiminjun.common.util.suspendRunCatching
import com.choiminjun.domain.repository.AlarmRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmRingViewModel @Inject constructor(
    private val alarmRepository: AlarmRepository,
) : BaseViewModel<AlarmRingState, AlarmRingIntent, AlarmRingSideEffect>(
    initialState = AlarmRingState(),
) {
    init {
        observeAlarm()
    }

    override suspend fun handleIntent(intent: AlarmRingIntent) {
        when (intent) {
            AlarmRingIntent.ConfirmDismiss -> confirmDismiss()
        }
    }

    private fun observeAlarm() {
        viewModelScope.launch {
            alarmRepository.observeAlarm().collect { alarmInfo ->
                reduce { copy(alarmInfo = alarmInfo) }
            }
        }
    }

    private fun confirmDismiss() {
        viewModelScope.launch {
            suspendRunCatching { alarmRepository.clearAlarm() }
                .onSuccess { postSideEffect(AlarmRingSideEffect.NavigateToHome) }
        }
    }
}
