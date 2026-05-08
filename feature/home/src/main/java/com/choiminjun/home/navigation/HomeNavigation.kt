package com.choiminjun.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.choiminjun.home.home.HomeRoute
import com.choiminjun.home.node.BusNodeRoute
import com.choiminjun.home.route.BusRouteRoute
import com.choiminjun.navigation.HomeBaseRoute
import com.choiminjun.navigation.HomeGraph

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    navigate(HomeGraph.HomeRoute, navOptions)
}

fun NavGraphBuilder.homeGraph(
    navigateToBusRoute: (routeId: String, routeNo: String) -> Unit,
    navigateToBusNode: (nodeId: String, nodeName: String, nodeNo: String?) -> Unit,
    navigateBack: () -> Unit,
    navigateToHome: () -> Unit,
    navigateToAlarmSetting: (routeId: String) -> Unit,
) {
    navigation<HomeBaseRoute>(startDestination = HomeGraph.HomeRoute) {
        composable<HomeGraph.HomeRoute> {
            HomeRoute(
                navigateToBusRoute = navigateToBusRoute,
                navigateToBusNode = navigateToBusNode,
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
                onAlarmClick = navigateToAlarmSetting,
                navigateToBusRoute = navigateToBusRoute,
                navigateToHome = navigateToHome,
            )
        }
    }
}
