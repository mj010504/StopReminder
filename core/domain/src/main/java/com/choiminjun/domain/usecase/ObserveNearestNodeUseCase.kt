package com.choiminjun.domain.usecase

import com.choiminjun.domain.model.location.NearestNodeResult
import com.choiminjun.domain.repository.LocationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ObserveNearestNodeUseCase @Inject constructor(
    private val getNodesByRoute: GetNodesByRouteUseCase,
    private val locationRepository: LocationRepository,
) {
    operator fun invoke(routeId: String, boardingNodeId: String, destNodeId: String): Flow<NearestNodeResult> = flow {
        val allNodes = getNodesByRoute(routeId)
            .filter { it.latitude != null && it.longitude != null }

        val boardingIndex = allNodes.indexOfFirst { it.nodeId == boardingNodeId }
        val destIndex = allNodes.indexOfFirst { it.nodeId == destNodeId }

        if (boardingIndex == -1 || destIndex == -1 || boardingIndex >= destIndex) return@flow

        emitAll(locationRepository.observeNearestNode(allNodes, boardingIndex, destIndex))
    }
}
