package com.choiminjun.data.di

import com.choiminjun.data.repository.BusRepositoryImpl
import com.choiminjun.data.repository.RecentSearchRepositoryImpl
import com.choiminjun.domain.repository.BusRepository
import com.choiminjun.domain.repository.RecentSearchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    @Singleton
    abstract fun bindBusRepository(busRepositoryImpl: BusRepositoryImpl): BusRepository

    @Binds
    @Singleton
    abstract fun bindRecentSearchRepository(
        impl: RecentSearchRepositoryImpl,
    ): RecentSearchRepository
}
