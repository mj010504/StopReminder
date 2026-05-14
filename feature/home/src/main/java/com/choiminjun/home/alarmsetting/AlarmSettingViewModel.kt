package com.choiminjun.home.alarmsetting

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.choiminjun.base.BaseViewModel
import com.choiminjun.common.util.suspendRunCatching
import com.choiminjun.domain.model.alarm.AlarmInfo
import com.choiminjun.domain.model.bus.BusNode
import com.choiminjun.domain.model.bus.CityCode
import com.choiminjun.domain.repository.AlarmRepository
import com.choiminjun.domain.repository.BusRepository
import com.choiminjun.navigation.HomeGraph
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmSettingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val busRepository: BusRepository,
    private val alarmRepository: AlarmRepository,
) : BaseViewModel<AlarmSettingState, AlarmSettingIntent, AlarmSettingSideEffect>(
    initialState = AlarmSettingState(),
) {
    init {
        val route = savedStateHandle.toRoute<HomeGraph.AlarmSettingRoute>()
        reduce { copy(routeNo = route.routeNo, routeId = route.routeId) }
        loadNodes(route.routeId)
    }

    override suspend fun handleIntent(intent: AlarmSettingIntent) {
        when (intent) {
            AlarmSettingIntent.ClickBack -> postSideEffect(AlarmSettingSideEffect.NavigateBack)
            is AlarmSettingIntent.SelectNode -> selectNode(intent.node)
            AlarmSettingIntent.DismissBottomSheet -> dismissBottomSheet()
            AlarmSettingIntent.ConfirmAlarm -> confirmAlarm()
            is AlarmSettingIntent.SelectStopsBefore -> reduce { copy(selectedStopsBefore = intent.stops) }
        }
    }

    private fun selectNode(node: BusNode) {
        reduce { copy(selectedNode = node) }
    }

    private fun dismissBottomSheet() {
        reduce { copy(selectedNode = null) }
    }

    private fun confirmAlarm() {
        val node = state.value.selectedNode ?: return
        viewModelScope.launch {
            suspendRunCatching {
                alarmRepository.setAlarm(
                    AlarmInfo(
                        routeId = state.value.routeId,
                        routeNo = state.value.routeNo,
                        destNodeId = node.nodeId,
                        destNodeName = node.nodeName,
                        stopsBeforeAlarm = state.value.selectedStopsBefore,
                    ),
                )
            }.onSuccess {
                postSideEffect(
                    AlarmSettingSideEffect.AlarmConfirmed(
                        routeNo = state.value.routeNo,
                        nodeName = node.nodeName,
                    ),
                )
            }
        }
    }

    private fun loadNodes(routeId: String) = viewModelScope.launch {
        reduce { copy(isLoading = true) }
        val nodes = suspendRunCatching { busRepository.getNodesByRoute(CityCode.BUSAN, routeId) }
            .getOrElse { emptyList() }
        reduce { copy(isLoading = false, nodes = nodes) }
    }
}
