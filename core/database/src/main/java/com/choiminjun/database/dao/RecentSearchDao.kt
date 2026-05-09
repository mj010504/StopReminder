package com.choiminjun.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.choiminjun.database.model.RecentNodeEntity
import com.choiminjun.database.model.RecentRouteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentSearchDao {
    @Query("SELECT * FROM recent_route ORDER BY id DESC")
    fun getAllRoutes(): Flow<List<RecentRouteEntity>>

    @Insert
    suspend fun insertRoute(entity: RecentRouteEntity)

    @Query("DELETE FROM recent_route WHERE id = :id")
    suspend fun deleteRoute(id: Long)

    @Query("SELECT * FROM recent_node ORDER BY id DESC")
    fun getAllNodes(): Flow<List<RecentNodeEntity>>

    @Insert
    suspend fun insertNode(entity: RecentNodeEntity)

    @Query("DELETE FROM recent_node WHERE id = :id")
    suspend fun deleteNode(id: Long)
}
