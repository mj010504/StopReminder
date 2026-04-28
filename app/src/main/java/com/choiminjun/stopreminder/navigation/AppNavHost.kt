package com.choiminjun.stopreminder.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.choiminjun.home.navigation.homeGraph
import com.choiminjun.navigation.HomeBaseRoute

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
        homeGraph()
    }
}
