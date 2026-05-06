package com.choiminjun.network.di

import com.choiminjun.network.BuildConfig
import com.choiminjun.network.adapter.TagoCallAdapterFactory
import com.choiminjun.network.api.TagoBusNodeApi
import com.choiminjun.network.api.TagoBusRouteApi
import com.choiminjun.network.interceptor.TagoBusRouteInterceptor
import com.choiminjun.network.interceptor.TagoBusStationInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    @Singleton
    @Provides
    fun provideJson(): Json = Json { ignoreUnknownKeys = true }

    @Singleton
    @Provides
    @TagoBusRouteClient
    fun provideTagoBusRouteClient(interceptor: TagoBusRouteInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .apply {
                if (BuildConfig.DEBUG) {
                    addInterceptor(
                        HttpLoggingInterceptor().apply {
                            level = HttpLoggingInterceptor.Level.BODY
                        },
                    )
                }
            }
            .build()

    @Singleton
    @Provides
    @TagoBusNodeClient
    fun provideTagoBusNodeClient(interceptor: TagoBusStationInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .apply {
                if (BuildConfig.DEBUG) {
                    addInterceptor(
                        HttpLoggingInterceptor().apply {
                            level = HttpLoggingInterceptor.Level.BODY
                        },
                    )
                }
            }
            .build()

    @Singleton
    @Provides
    fun provideTagoBusRouteApi(
        json: Json,
        @TagoBusRouteClient okHttpClient: OkHttpClient,
        callAdapterFactory: TagoCallAdapterFactory,
    ): TagoBusRouteApi = Retrofit.Builder()
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BuildConfig.TAGO_BASE_URL)
        .addCallAdapterFactory(callAdapterFactory)
        .build()
        .create(TagoBusRouteApi::class.java)

    @Singleton
    @Provides
    fun provideTagoBusNodeApi(
        json: Json,
        @TagoBusNodeClient okHttpClient: OkHttpClient,
        callAdapterFactory: TagoCallAdapterFactory,
    ): TagoBusNodeApi = Retrofit.Builder()
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BuildConfig.TAGO_BASE_URL)
        .addCallAdapterFactory(callAdapterFactory)
        .build()
        .create(TagoBusNodeApi::class.java)
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TagoBusRouteClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TagoBusNodeClient
