package com.choiminjun.network.adapter

import android.util.Log
import com.choiminjun.network.error.parseTagoError
import com.choiminjun.network.model.TagoBaseResponse
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.CallAdapter.Factory.getParameterUpperBound
import retrofit2.CallAdapter.Factory.getRawType
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TagoCallAdapterFactory @Inject constructor() : CallAdapter.Factory() {
    override fun get(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit,
    ): CallAdapter<*, *>? {
        if (getRawType(type) != Call::class.java) return null

        val wrapperType = getParameterUpperBound(0, type as ParameterizedType)
        if (getRawType(wrapperType) != TagoBaseResponse::class.java) return null

        return TagoCallAdapter(wrapperType)
    }
}

private class TagoCallAdapter(
    private val resultType: Type,
) : CallAdapter<Any, Call<Any>> {
    override fun responseType(): Type = resultType
    override fun adapt(call: Call<Any>): Call<Any> = TagoCall(call)
}

private class TagoCall<T : Any>(
    private val delegate: Call<T>,
) : Call<T> {
    override fun enqueue(callback: Callback<T>) {
        delegate.enqueue(
            object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()

                    if (response.isSuccessful && body != null) {
                        callback.onResponse(this@TagoCall, response)
                    } else {
                        val errorBody = response.errorBody()?.string() ?: ""
                        val exception = parseTagoError(errorBody, response.code())

                        Log.e("TagoCallAdapterFactory", "API Error: ${exception.message}")
                        callback.onFailure(this@TagoCall, exception)
                    }
                }

                override fun onFailure(call: Call<T>, throwable: Throwable) {
                    callback.onFailure(this@TagoCall, throwable)
                }
            },
        )
    }

    override fun clone(): Call<T> = TagoCall(delegate.clone())
    override fun execute(): Response<T> =
        throw NotImplementedError("SRCall doesn't support execute()")

    override fun isExecuted(): Boolean = delegate.isExecuted
    override fun cancel() = delegate.cancel()
    override fun isCanceled(): Boolean = delegate.isCanceled
    override fun request(): Request = delegate.request()
    override fun timeout(): Timeout = delegate.timeout()
}
