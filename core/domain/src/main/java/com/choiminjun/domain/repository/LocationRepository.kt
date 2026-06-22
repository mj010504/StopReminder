package com.choiminjun.domain.repository

import com.choiminjun.domain.model.bus.BusNode
import com.choiminjun.domain.model.location.Coordinate
import com.choiminjun.domain.model.location.NearestNodeResult
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun observeLocation(): Flow<Coordinate>
    fun observeNearestNode(
        nodes: List<BusNode>,
        boardingIndex: Int,
        destIndex: Int,
    ): Flow<NearestNodeResult>
}
