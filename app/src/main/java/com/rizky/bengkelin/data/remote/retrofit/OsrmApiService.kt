package com.rizky.bengkelin.data.remote.retrofit

import com.rizky.bengkelin.data.remote.response.OsrmResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OsrmApiService {
    @GET("driving/{userLong},{userLat};{desLong},{desLat}")
    suspend fun calculateDistance(
        @Path("userLong") userLong: Double,
        @Path("userLat") userLat: Double,
        @Path("desLong") desLong: Double,
        @Path("desLat") desLat: Double,
        @Query("overview") overview: Boolean,
        @Query("skip_waypoints") skipWaypoints: Boolean
    ): Response<OsrmResponse>
}

object OsrmApiConfig {
    fun getApiService(): OsrmApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://router.project-osrm.org/route/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(OsrmApiService::class.java)
    }
}