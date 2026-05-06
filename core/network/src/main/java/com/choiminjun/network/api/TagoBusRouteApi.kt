package com.choiminjun.network.api

import com.choiminjun.network.model.GetNodesByRouteResponse
import com.choiminjun.network.model.GetRouteNumbersResponse
import com.choiminjun.network.model.TagoBaseResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TagoBusRouteApi {
    @GET("BusRouteInfoInqireService/getRouteNoList")
    suspend fun getRouteNumbers(
        @Query("numOfRows") size: Int,
        @Query("pageNo") page: Int,
        @Query("cityCode") cityCode: Int,
        @Query("routeNo") routeNo: String,
    ): TagoBaseResponse<GetRouteNumbersResponse>

    @GET("BusRouteInfoInqireService/getRouteAcctoThrghSttnList")
    suspend fun getNodesByRoute(
        @Query("numOfRows") size: Int,
        @Query("pageNo") page: Int,
        @Query("cityCode") cityCode: Int,
        @Query("routeId") routeId: String,
    ): TagoBaseResponse<GetNodesByRouteResponse>
}
