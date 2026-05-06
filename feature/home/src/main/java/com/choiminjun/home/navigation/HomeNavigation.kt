package com.choiminjun.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.choiminjun.home.home.HomeRoute
import com.choiminjun.home.node.BusNodeScreen
import com.choiminjun.home.route.BusRouteScreen
import com.choiminjun.navigation.HomeBaseRoute
import com.choiminjun.navigation.HomeGraph

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    navigate(HomeGraph.HomeRoute, navOptions)
}

fun NavGraphBuilder.homeGraph(
    navigateToBusRoute: (routeId: String) -> Unit,
    navigateToBusNode: (nodeId: String) -> Unit,
    navigateBack: () -> Unit,
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
            BusRouteScreen(onBackClick = navigateBack)
        }
        composable<HomeGraph.BusNodeRoute> {
            BusNodeScreen(
                onBackClick = navigateBack,
                onAlarmClick = navigateToAlarmSetting,
            )
        }
    }
}
