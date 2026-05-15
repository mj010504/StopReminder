package com.choiminjun.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.choiminjun.database.model.FavoriteNodeEntity
import com.choiminjun.database.model.FavoriteRouteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorite_route")
    fun getAllRoutes(): Flow<List<FavoriteRouteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoute(entity: FavoriteRouteEntity)

    @Query("DELETE FROM favorite_route WHERE routeId = :routeId")
    suspend fun deleteRoute(routeId: String)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_route WHERE routeId = :routeId)")
    fun isFavoriteRoute(routeId: String): Flow<Boolean>

    @Query("SELECT * FROM favorite_node")
    fun getAllNodes(): Flow<List<FavoriteNodeEntity>>

    @Insert
    suspend fun insertNode(entity: FavoriteNodeEntity)

    @Query("DELETE FROM favorite_node WHERE nodeId = :nodeId")
    suspend fun deleteNode(nodeId: String)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_node WHERE nodeId = :nodeId)")
    fun isFavoriteNode(nodeId: String): Flow<Boolean>
}
