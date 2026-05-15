package com.choiminjun.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.choiminjun.database.dao.FavoriteDao
import com.choiminjun.database.dao.RecentSearchDao
import com.choiminjun.database.model.FavoriteNodeEntity
import com.choiminjun.database.model.FavoriteRouteEntity
import com.choiminjun.database.model.RecentNodeEntity
import com.choiminjun.database.model.RecentRouteEntity

@Database(
    entities = [RecentRouteEntity::class, RecentNodeEntity::class, FavoriteRouteEntity::class, FavoriteNodeEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class SRDatabase : RoomDatabase() {
    abstract fun recentSearchDao(): RecentSearchDao
    abstract fun favoriteDao(): FavoriteDao

    companion object {
        internal const val NAME = "stopreminder-database"
    }
}
