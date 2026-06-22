package com.choiminjun.stopreminder.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.choiminjun.alarm.navigation.alarmGraph
import com.choiminjun.home.navigation.homeGraph
import com.choiminjun.navigation.HomeBaseRoute
import com.choiminjun.navigation.HomeGraph
import com.choiminjun.stopreminder.service.startAlarmService
import com.choiminjun.stopreminder.service.stopAlarmService

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = HomeBaseRoute,
        modifier = modifier,
    ) {
        homeGraph(
            navigateToBusRoute = { busRoute ->
                navController.navigate(
                    HomeGraph.BusRouteRoute(
                        busRoute.routeId,
                        busRoute.routeNo,
                        busRoute.routeType,
                        busRoute.startNodeName,
                        busRoute.endNodeName,
                        busRoute.cityCode.name,
                    ),
                )
            },
            navigateToBusNode = { busNode ->
                navController.navigate(
                    HomeGraph.BusNodeRoute(busNode.nodeId, busNode.nodeName, busNode.nodeNo, busNode.cityCode.name),
                )
            },
            navigateBack = { navController.popBackStack() },
            navigateToHome = {
                navController.navigate(HomeGraph.HomeRoute) {
                    popUpTo(HomeBaseRoute) { inclusive = false }
                }
            },
            navigateToAlarmSetting = { routeId, routeNo, boardingNodeId, boardingNodeName ->
                navController.navigate(HomeGraph.AlarmSettingRoute(routeId, routeNo, boardingNodeId, boardingNodeName))
            },
            navigateToAlarmMonitor = {
                navController.navigate(HomeGraph.AlarmMonitorRoute) {
                    popUpTo(HomeGraph.HomeRoute) { inclusive = false }
                }
            },
            navigateToAlarmRing = {
                navController.navigate(HomeGraph.AlarmRingRoute) {
                    launchSingleTop = true
                }
            },
        )
        alarmGraph(
            navigateBack = { navController.popBackStack() },
            onAlarmSet = {
                navController.navigate(HomeGraph.AlarmMonitorRoute) {
                    popUpTo(HomeGraph.HomeRoute) { inclusive = false }
                }
            },
            onAlarmStopped = {
                navController.navigate(HomeGraph.HomeRoute) {
                    popUpTo(HomeBaseRoute) { inclusive = false }
                }
            },
            navigateToAlarmRing = {
                navController.navigate(HomeGraph.AlarmRingRoute) {
                    launchSingleTop = true
                }
            },
            onAlarmDismissed = {
                navController.navigate(HomeGraph.HomeRoute) {
                    popUpTo(HomeBaseRoute) { inclusive = false }
                }
            },
            startAlarmService = { routeNo, nodeName -> startAlarmService(context, routeNo, nodeName) },
            stopAlarmService = { stopAlarmService(context) },
        )
    }
}
