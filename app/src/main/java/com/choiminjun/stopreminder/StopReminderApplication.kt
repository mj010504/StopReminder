package com.choiminjun.stopreminder

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class StopReminderApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
