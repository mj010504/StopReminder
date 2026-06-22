package com.choiminjun.domain.repository

import com.choiminjun.domain.model.alarm.AlarmInfo
import com.choiminjun.domain.model.location.NearestNodeResult
import kotlinx.coroutines.flow.Flow

interface AlarmRepository {
    fun observeAlarm(): Flow<AlarmInfo>
    suspend fun setAlarm(alarm: AlarmInfo)
    suspend fun clearAlarm()
    suspend fun triggerAlarm()
    fun observeNearestNode(): Flow<NearestNodeResult?>
    fun updateNearestNode(result: NearestNodeResult)
}
