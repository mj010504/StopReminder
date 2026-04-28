package com.choiminjun.stopreminder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.choiminjun.designsystem.theme.StopReminderTheme
import com.choiminjun.stopreminder.ui.StopReminderApp
import com.choiminjun.stopreminder.ui.rememberAppState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val appState = rememberAppState(navController)

            StopReminderTheme {
                StopReminderApp(appState = appState)
            }
        }
    }
}
