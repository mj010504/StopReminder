package com.choiminjun.database.di

import android.content.Context
import androidx.room.Room
import com.choiminjun.database.SRDatabase
import com.choiminjun.database.dao.FavoriteDao
import com.choiminjun.database.dao.RecentSearchDao
import com.choiminjun.database.source.favorite.FavoriteDataSource
import com.choiminjun.database.source.favorite.FavoriteDataSourceImpl
import com.choiminjun.database.source.recentsearch.RecentSearchDataSource
import com.choiminjun.database.source.recentsearch.RecentSearchDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DatabaseModule {
    @Binds
    @Singleton
    abstract fun bindRecentSearchDataSource(
        impl: RecentSearchDataSourceImpl,
    ): RecentSearchDataSource

    @Binds
    @Singleton
    abstract fun bindFavoriteDataSource(
        impl: FavoriteDataSourceImpl,
    ): FavoriteDataSource

    companion object {
        @Provides
        @Singleton
        fun provideSRDatabase(@ApplicationContext context: Context): SRDatabase =
            Room.databaseBuilder(context, SRDatabase::class.java, SRDatabase.NAME)
                .fallbackToDestructiveMigration(dropAllTables = true)
                .build()

        @Provides
        @Singleton
        fun provideRecentSearchDao(db: SRDatabase): RecentSearchDao = db.recentSearchDao()

        @Provides
        @Singleton
        fun provideFavoriteDao(db: SRDatabase): FavoriteDao = db.favoriteDao()
    }
}
