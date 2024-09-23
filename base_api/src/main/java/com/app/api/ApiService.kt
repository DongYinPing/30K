package com.app.api

import com.app.network.ApiNetwork
import retrofit2.http.GET

val Api by lazy { ApiNetwork.createService(ApiService::class.java) }

interface ApiService {
    @GET("/test")
    suspend fun test(): ApiResponse<String>
}
