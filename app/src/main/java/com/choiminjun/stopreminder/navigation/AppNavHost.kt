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
            navigateToBusRoute = { routeId ->
                navController.navigate(HomeGraph.BusRouteRoute(routeId))
            },
            navigateToBusNode = { nodeId ->
                navController.navigate(HomeGraph.BusNodeRoute(nodeId))
            },
            navigateBack = { navController.popBackStack() },
            navigateToAlarmSetting = { _ ->
                // TODO: 알람 설정 화면으로 이동
            },
        )
    }
}
