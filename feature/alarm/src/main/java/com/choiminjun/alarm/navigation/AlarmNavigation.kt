package com.choiminjun.alarm.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.choiminjun.alarm.alarmmonitor.AlarmMonitorRoute
import com.choiminjun.alarm.alarmring.AlarmRingRoute
import com.choiminjun.alarm.alarmsetting.AlarmSettingRoute
import com.choiminjun.navigation.HomeGraph

fun NavGraphBuilder.alarmGraph(
    navigateBack: () -> Unit,
    onAlarmSet: () -> Unit,
    onAlarmStopped: () -> Unit,
    navigateToAlarmRing: () -> Unit,
    onAlarmDismissed: () -> Unit,
    startAlarmService: (routeNo: String, nodeName: String) -> Unit,
    stopAlarmService: () -> Unit,
) {
    composable<HomeGraph.AlarmSettingRoute> {
        AlarmSettingRoute(
            onBackClick = navigateBack,
            onAlarmSet = { routeNo, nodeName ->
                startAlarmService(routeNo, nodeName)
                onAlarmSet()
            },
        )
    }
    composable<HomeGraph.AlarmMonitorRoute> {
        AlarmMonitorRoute(
            onBackClick = navigateBack,
            onAlarmStopped = {
                stopAlarmService()
                onAlarmStopped()
            },
            navigateToAlarmRing = navigateToAlarmRing,
        )
    }
    composable<HomeGraph.AlarmRingRoute> {
        AlarmRingRoute(
            onDismiss = {
                stopAlarmService()
                onAlarmDismissed()
            },
        )
    }
}
