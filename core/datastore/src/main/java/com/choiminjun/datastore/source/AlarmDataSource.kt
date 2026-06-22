package com.choiminjun.datastore.source

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.choiminjun.domain.model.alarm.AlarmInfo
import com.choiminjun.domain.model.location.NearestNodeResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Named

class AlarmDataSource @Inject constructor(
    @Named("alarm") private val dataStore: DataStore<Preferences>,
) {
    val alarmInfo: Flow<AlarmInfo> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { prefs ->
            AlarmInfo(
                routeId = prefs[KEY_ROUTE_ID] ?: "",
                routeNo = prefs[KEY_ROUTE_NO] ?: "",
                destNodeId = prefs[KEY_DEST_NODE_ID] ?: "",
                destNodeName = prefs[KEY_DEST_NODE_NAME] ?: "",
                boardingNodeId = prefs[KEY_BOARDING_NODE_ID] ?: "",
                boardingNodeName = prefs[KEY_BOARDING_NODE_NAME] ?: "",
                stopsBeforeAlarm = prefs[KEY_STOPS_BEFORE] ?: 1,
                isTriggered = prefs[KEY_IS_TRIGGERED] ?: false,
            )
        }

    suspend fun setAlarm(alarm: AlarmInfo) {
        dataStore.edit { prefs ->
            prefs[KEY_ROUTE_ID] = alarm.routeId
            prefs[KEY_ROUTE_NO] = alarm.routeNo
            prefs[KEY_DEST_NODE_ID] = alarm.destNodeId
            prefs[KEY_DEST_NODE_NAME] = alarm.destNodeName
            prefs[KEY_BOARDING_NODE_ID] = alarm.boardingNodeId
            prefs[KEY_BOARDING_NODE_NAME] = alarm.boardingNodeName
            prefs[KEY_STOPS_BEFORE] = alarm.stopsBeforeAlarm
        }
    }

    suspend fun clearAlarm() {
        dataStore.edit { it.clear() }
    }

    suspend fun triggerAlarm() {
        dataStore.edit { prefs ->
            prefs[KEY_IS_TRIGGERED] = true
        }
    }

    private val _nearestNode = MutableStateFlow<NearestNodeResult?>(null)
    val nearestNode: StateFlow<NearestNodeResult?> = _nearestNode.asStateFlow()

    fun updateNearestNode(result: NearestNodeResult) {
        _nearestNode.value = result
    }

    private companion object {
        val KEY_ROUTE_ID = stringPreferencesKey("route_id")
        val KEY_ROUTE_NO = stringPreferencesKey("route_no")
        val KEY_DEST_NODE_ID = stringPreferencesKey("dest_node_id")
        val KEY_DEST_NODE_NAME = stringPreferencesKey("dest_node_name")
        val KEY_BOARDING_NODE_ID = stringPreferencesKey("boarding_node_id")
        val KEY_BOARDING_NODE_NAME = stringPreferencesKey("boarding_node_name")
        val KEY_STOPS_BEFORE = intPreferencesKey("stops_before")
        val KEY_IS_TRIGGERED = booleanPreferencesKey("is_triggered")
    }
}
