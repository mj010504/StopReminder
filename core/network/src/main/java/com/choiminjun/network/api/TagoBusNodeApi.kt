package com.choiminjun.network.api

import com.choiminjun.network.model.GetNodeNumbersResponse
import com.choiminjun.network.model.GetRoutesByNodeResponse
import com.choiminjun.network.model.TagoBaseResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TagoBusNodeApi {
    @GET("BusSttnInfoInqireService/getSttnNoList")
    suspend fun getNodeNumbers(
        @Query("numOfRows") size: Int,
        @Query("pageNo") page: Int,
        @Query("cityCode") cityCode: Int,
        @Query("nodeNm") nodeName: String,
    ): TagoBaseResponse<GetNodeNumbersResponse>

    @GET("BusSttnInfoInqireService/getSttnThrghRouteList")
    suspend fun getRoutesByNode(
        @Query("numOfRows") size: Int,
        @Query("pageNo") page: Int,
        @Query("cityCode") cityCode: Int,
        @Query("nodeid") nodeId: String,
    ): TagoBaseResponse<GetRoutesByNodeResponse>
}
