package com.choiminjun.stopreminder.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.choiminjun.home.navigation.homeGraph
import com.choiminjun.navigation.HomeBaseRoute
import com.choiminjun.navigation.HomeGraph

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HomeBaseRoute,
        modifier = modifier,
    ) {
        homeGraph(
            navigateToBusRoute = { routeId, routeNo, routeType, startNodeName, endNodeName, cityCode ->
                navController.navigate(HomeGraph.BusRouteRoute(routeId, routeNo, routeType, startNodeName, endNodeName, cityCode))
            },
            navigateToBusNode = { nodeId, nodeName, nodeNo, cityCode ->
                navController.navigate(HomeGraph.BusNodeRoute(nodeId, nodeName, nodeNo, cityCode))
            },
            navigateBack = { navController.popBackStack() },
            navigateToHome = {
                navController.navigate(HomeGraph.HomeRoute) {
                    popUpTo(HomeBaseRoute) { inclusive = false }
                }
            },
            navigateToAlarmSetting = { routeId, routeNo ->
                navController.navigate(HomeGraph.AlarmSettingRoute(routeId, routeNo))
            },
            navigateToAlarmRing = {
                navController.navigate(HomeGraph.AlarmRingRoute)
            },
        )
    }
}
