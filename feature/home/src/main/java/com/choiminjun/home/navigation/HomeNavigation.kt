package com.choiminjun.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.choiminjun.home.HomeRoute
import com.choiminjun.navigation.HomeBaseRoute
import com.choiminjun.navigation.HomeGraph

fun NavController.navigateToPractice(navOptions: NavOptions? = null) {
    navigate(HomeGraph.HomeRoute, navOptions)
}

fun NavGraphBuilder.homeGraph() {
    navigation<HomeBaseRoute>(startDestination = HomeGraph.HomeRoute) {
        composable<HomeGraph.HomeRoute> {
            HomeRoute()
        }
    }
}
