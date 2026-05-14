package com.choiminjun.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.choiminjun.database.model.RecentNodeEntity
import com.choiminjun.database.model.RecentRouteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentSearchDao {
    @Query("SELECT * FROM recent_route ORDER BY id DESC")
    fun getAllRoutes(): Flow<List<RecentRouteEntity>>

    @Insert
    suspend fun insertRoute(entity: RecentRouteEntity)

    @Query("DELETE FROM recent_route WHERE routeId = :routeId")
    suspend fun deleteRouteByRouteId(routeId: String)

    @Query("DELETE FROM recent_route WHERE id = :id")
    suspend fun deleteRoute(id: Long)

    @Transaction
    suspend fun upsertRoute(entity: RecentRouteEntity) {
        deleteRouteByRouteId(entity.routeId)
        insertRoute(entity)
    }

    @Query("SELECT * FROM recent_node ORDER BY id DESC")
    fun getAllNodes(): Flow<List<RecentNodeEntity>>

    @Insert
    suspend fun insertNode(entity: RecentNodeEntity)

    @Query("DELETE FROM recent_node WHERE nodeId = :nodeId")
    suspend fun deleteNodeByNodeId(nodeId: String)

    @Query("DELETE FROM recent_node WHERE id = :id")
    suspend fun deleteNode(id: Long)

    @Transaction
    suspend fun upsertNode(entity: RecentNodeEntity) {
        deleteNodeByNodeId(entity.nodeId)
        insertNode(entity)
    }
}
