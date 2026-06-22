package com.choiminjun.domain.usecase

import com.choiminjun.domain.model.bus.BusNode
import com.choiminjun.domain.model.bus.CityCode
import com.choiminjun.domain.repository.BusRepository
import javax.inject.Inject

class GetNodesByRouteUseCase @Inject constructor(
    private val busRepository: BusRepository,
) {
    suspend operator fun invoke(routeId: String): List<BusNode> =
        busRepository.getNodesByRoute(CityCode.BUSAN, routeId)
}
