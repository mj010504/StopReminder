package com.choiminjun.alarm.alarmmonitor

import androidx.lifecycle.viewModelScope
import com.choiminjun.base.BaseViewModel
import com.choiminjun.common.util.suspendRunCatching
import com.choiminjun.domain.model.alarm.AlarmInfo
import com.choiminjun.domain.repository.AlarmRepository
import com.choiminjun.domain.usecase.GetNodesByRouteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmMonitorViewModel @Inject constructor(
    private val alarmRepository: AlarmRepository,
    private val getNodesByRoute: GetNodesByRouteUseCase,
) : BaseViewModel<AlarmMonitorState, AlarmMonitorIntent, AlarmMonitorSideEffect>(
    initialState = AlarmMonitorState(),
) {
    init {
        observeAlarm()
        observeNearestNode()
    }

    override suspend fun handleIntent(intent: AlarmMonitorIntent) {
        when (intent) {
            AlarmMonitorIntent.ClickBack -> postSideEffect(AlarmMonitorSideEffect.NavigateBack)
            AlarmMonitorIntent.StopAlarm -> stopAlarm()
        }
    }

    private fun observeAlarm() {
        viewModelScope.launch {
            alarmRepository.observeAlarm().collect { alarmInfo ->
                if (alarmInfo.isTriggered) {
                    postSideEffect(AlarmMonitorSideEffect.AlarmTriggered)
                } else {
                    reduce { copy(alarmInfo = alarmInfo) }
                    if (state.value.routeNodeNames.isEmpty() && alarmInfo.routeId.isNotBlank()) {
                        loadRouteNodes(alarmInfo)
                    }
                }
            }
        }
    }

    private fun observeNearestNode() {
        viewModelScope.launch {
            alarmRepository.observeNearestNode().collect { result ->
                reduce {
                    copy(
                        nearestNodeName = result?.nearestNodeName,
                        remainingStops = result?.remaining,
                    )
                }
            }
        }
    }

    private fun loadRouteNodes(alarmInfo: AlarmInfo) {
        viewModelScope.launch {
            val nodes = suspendRunCatching { getNodesByRoute(alarmInfo.routeId) }
                .getOrElse { emptyList() }

            val boardingIndex = nodes.indexOfFirst { it.nodeId == alarmInfo.boardingNodeId }
            val destIndex = nodes.indexOfFirst { it.nodeId == alarmInfo.destNodeId }

            if (boardingIndex == -1 || destIndex == -1 || boardingIndex >= destIndex) {
                reduce { copy(isLoadingNodes = false) }
                return@launch
            }

            val nodeNames = nodes.subList(boardingIndex, destIndex + 1).map { it.nodeName }
            reduce { copy(routeNodeNames = nodeNames, isLoadingNodes = false) }
        }
    }

    private fun stopAlarm() {
        viewModelScope.launch {
            suspendRunCatching { alarmRepository.clearAlarm() }
                .onSuccess { postSideEffect(AlarmMonitorSideEffect.AlarmStopped) }
        }
    }
}
