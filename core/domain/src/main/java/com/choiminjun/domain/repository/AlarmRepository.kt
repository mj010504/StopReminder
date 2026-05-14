package com.choiminjun.domain.repository

import com.choiminjun.domain.model.alarm.AlarmInfo
import kotlinx.coroutines.flow.Flow

interface AlarmRepository {
    fun observeAlarm(): Flow<AlarmInfo>
    suspend fun setAlarm(alarm: AlarmInfo)
    suspend fun clearAlarm()
}
