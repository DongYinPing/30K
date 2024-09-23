package com.app.network

import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * 网络请求
 */
object ApiNetwork {
    private var isDebug: Boolean = true

    private var baseUrl: String = ""

    var token: String? = null
        private set

    fun initBaseUrl(
        url: String,
        debug: Boolean,
    ) {
        baseUrl = url
        isDebug = debug
    }

    fun updateToken(userToken: String? = "") {
        token = userToken
    }

    fun <T> createService(serviceClass: Class<T>): T {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .callFactory(callFactory)
            .addConverterFactory(converterFactory)
            .build()
            .create(serviceClass)
    }

    private val callFactory by lazy {
        val client =
            OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(
                    HttpLoggingInterceptor()
                        .apply {
                            level =
                                if (isDebug) {
                                    HttpLoggingInterceptor.Level.BODY
                                } else {
                                    HttpLoggingInterceptor.Level.NONE
                                }
                        },
                )
                .build()
        CustomCallFactory(client)
    }

    private val converterFactory by lazy {
        GsonConverterFactory.create()
    }
}

// 自定义
class CustomCallFactory(private val delegate: Call.Factory) : Call.Factory {
    override fun newCall(request: Request): Call {
        val builder = request.newBuilder()
        val token = ApiNetwork.token
        request.url().toString().let {
            builder.header("TimeZone", "GMT+8")
            if (!token.isNullOrEmpty()) {
                builder.header("Blade-Auth", "bearer $token")
            }
        }
        return delegate.newCall(builder.build())
    }
}
