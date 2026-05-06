package com.choiminjun.network.interceptor

import com.choiminjun.network.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class TagoBusRouteInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url

        val newUrl = originalUrl.newBuilder()
            .addQueryParameter("serviceKey", BuildConfig.TAGO_BUS_ROUTE_KEY)
            .addQueryParameter("_type", "json")
            .build()

        val newRequest = originalRequest.newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(newRequest)
    }
}
