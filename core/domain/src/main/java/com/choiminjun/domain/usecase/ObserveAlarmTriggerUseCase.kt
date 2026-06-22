package com.choiminjun.domain.usecase

import com.choiminjun.domain.model.alarm.AlarmInfo
import com.choiminjun.domain.model.location.NearestNodeResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.take
import javax.inject.Inject

class ObserveAlarmTriggerUseCase @Inject constructor(
    private val observeNearestNode: ObserveNearestNodeUseCase,
) {
    operator fun invoke(alarmInfo: AlarmInfo): Flow<NearestNodeResult> =
        observeNearestNode(alarmInfo.routeId, alarmInfo.boardingNodeId, alarmInfo.destNodeId)
            .filter { it.remaining in 0..alarmInfo.stopsBeforeAlarm }
            .take(1)
}
