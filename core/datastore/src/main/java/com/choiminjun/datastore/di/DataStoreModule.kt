package com.choiminjun.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    private const val ALARM_DATASTORE_NAME = "alarm_preferences"
    private val Context.alarmDataStore by preferencesDataStore(name = ALARM_DATASTORE_NAME)

    @Provides
    @Singleton
    @Named("alarm")
    fun provideAlarmDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> = context.alarmDataStore
}
