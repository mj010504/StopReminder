package com.choiminjun.data.repository

import com.choiminjun.datastore.source.AlarmDataSource
import com.choiminjun.domain.model.alarm.AlarmInfo
import com.choiminjun.domain.repository.AlarmRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlarmRepositoryImpl @Inject constructor(
    private val alarmDataSource: AlarmDataSource,
) : AlarmRepository {
    override fun observeAlarm(): Flow<AlarmInfo> = alarmDataSource.alarmInfo

    override suspend fun setAlarm(alarm: AlarmInfo) = alarmDataSource.setAlarm(alarm)

    override suspend fun clearAlarm() = alarmDataSource.clearAlarm()
}
