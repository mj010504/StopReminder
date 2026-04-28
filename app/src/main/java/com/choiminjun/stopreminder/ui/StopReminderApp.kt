package com.choiminjun.stopreminder.ui

import androidx.compose.runtime.Composable
import com.choiminjun.stopreminder.navigation.AppNavHost

@Composable
internal fun StopReminderApp(
    appState: AppState,
) {
    AppNavHost(
        navController = appState.navController,
    )
}
