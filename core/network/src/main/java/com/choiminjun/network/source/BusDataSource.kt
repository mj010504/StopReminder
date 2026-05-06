package com.choiminjun.network.source

import com.choiminjun.network.api.TagoBusNodeApi
import com.choiminjun.network.api.TagoBusRouteApi
import com.choiminjun.network.model.GetNodeNumbersItem
import com.choiminjun.network.model.GetNodesByRouteItem
import com.choiminjun.network.model.GetRouteNumbersItem
import com.choiminjun.network.model.GetRoutesByNodeItem
import com.choiminjun.network.model.getData
import javax.inject.Inject

class BusDataSource @Inject constructor(
    private val busRouteApi: TagoBusRouteApi,
    private val busNodeApi: TagoBusNodeApi,
) {
    suspend fun getRouteNumbers(cityCode: Int, routeNo: String): List<GetRouteNumbersItem> {
        val allItems = mutableListOf<GetRouteNumbersItem>()
        var page = 1
        while (true) {
            val response = busRouteApi.getRouteNumbers(
                size = PAGE_SIZE,
                page = page,
                cityCode = cityCode,
                routeNo = routeNo,
            ).getData()
            if (response.items == null) return allItems

            allItems.addAll(response.items.item)
            if (allItems.size >= response.totalCount || response.items.item.isEmpty()) break
            page++
        }
        return allItems
    }

    suspend fun getNodesByRoute(cityCode: Int, routeId: String): List<GetNodesByRouteItem> {
        val allItems = mutableListOf<GetNodesByRouteItem>()
        var page = 1
        while (true) {
            val response = busRouteApi.getNodesByRoute(
                size = PAGE_SIZE_LARGE,
                page = page,
                cityCode = cityCode,
                routeId = routeId,
            ).getData()
            if (response.items == null) return allItems

            allItems.addAll(response.items.item)
            if (allItems.size >= response.totalCount || response.items.item.isEmpty()) break
            page++
        }
        return allItems
    }

    suspend fun getNodeNumbers(cityCode: Int, nodeName: String): List<GetNodeNumbersItem> {
        val allItems = mutableListOf<GetNodeNumbersItem>()
        var page = 1
        while (true) {
            val response = busNodeApi.getNodeNumbers(
                size = PAGE_SIZE,
                page = page,
                cityCode = cityCode,
                nodeName = nodeName,
            ).getData()
            if (response.items == null) return allItems

            allItems.addAll(response.items.item)
            if (allItems.size >= response.totalCount || response.items.item.isEmpty()) break
            page++
        }
        return allItems
    }

    suspend fun getRoutesByNode(cityCode: Int, nodeId: String): List<GetRoutesByNodeItem> =
        busNodeApi.getRoutesByNode(
            size = PAGE_SIZE_LARGE,
            page = 1,
            cityCode = cityCode,
            nodeId = nodeId,
        ).getData().items?.item ?: emptyList()

    private companion object {
        const val PAGE_SIZE = 50
        const val PAGE_SIZE_LARGE = 100
    }
}
