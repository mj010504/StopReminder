package com.choiminjun.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.choiminjun.domain.model.bus.BusNode
import com.choiminjun.domain.model.bus.BusRoute
import com.choiminjun.home.home.HomeRoute
import com.choiminjun.home.node.BusNodeRoute
import com.choiminjun.home.route.BusRouteRoute
import com.choiminjun.navigation.HomeBaseRoute
import com.choiminjun.navigation.HomeGraph

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    navigate(HomeGraph.HomeRoute, navOptions)
}

fun NavGraphBuilder.homeGraph(
    navigateToBusRoute: (BusRoute) -> Unit,
    navigateToBusNode: (BusNode) -> Unit,
    navigateBack: () -> Unit,
    navigateToHome: () -> Unit,
    navigateToAlarmSetting:
    (routeId: String, routeNo: String, boardingNodeId: String, boardingNodeName: String) -> Unit,
    navigateToAlarmMonitor: () -> Unit,
    navigateToAlarmRing: () -> Unit,
) {
    navigation<HomeBaseRoute>(startDestination = HomeGraph.HomeRoute) {
        composable<HomeGraph.HomeRoute> {
            HomeRoute(
                navigateToBusRoute = navigateToBusRoute,
                navigateToBusNode = navigateToBusNode,
                navigateToAlarmMonitor = navigateToAlarmMonitor,
                navigateToAlarmRing = navigateToAlarmRing,
            )
        }
        composable<HomeGraph.BusRouteRoute> {
            BusRouteRoute(
                onBackClick = navigateBack,
                navigateToBusNode = navigateToBusNode,
                navigateToHome = navigateToHome,
            )
        }
        composable<HomeGraph.BusNodeRoute> {
            BusNodeRoute(
                onBackClick = navigateBack,
                onAlarmClick = { routeId, routeNo, boardingNodeId, boardingNodeName ->
                    navigateToAlarmSetting(routeId, routeNo, boardingNodeId, boardingNodeName)
                },
                navigateToBusRoute = navigateToBusRoute,
                navigateToHome = navigateToHome,
            )
        }
    }
}
