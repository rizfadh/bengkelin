package com.rizky.bengkelin.data.remote.retrofit

import com.rizky.bengkelin.data.remote.response.*
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface BengkelinApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<CommonResponse>

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<LoginResponse>

    @GET("bengkels")
    suspend fun getBengkelList(): Response<BengkelListResponse>

    @GET("bengkels/{id}")
    suspend fun getBengkelDetail(
        @Path("id") id: Int
    ): Response<DetailResponse>
}

object BengkelinApiConfig {
    fun getApiService(): BengkelinApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://backend-dot-c23-ps474.et.r.appspot.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(BengkelinApiService::class.java)
    }
}