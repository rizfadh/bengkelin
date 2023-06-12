package com.rizky.bengkelin.data.remote.retrofit

import com.rizky.bengkelin.data.remote.response.PredictionResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface PredictionApiService {

    @Multipart
    @POST("predict")
    suspend fun getPrediction(
        @Part file: MultipartBody.Part
    ): Response<PredictionResponse>

}

object PredictionApiConfig {
    fun getApiService(): PredictionApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://bandetedcation-a4aqt2vbxa-uc.a.run.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(PredictionApiService::class.java)
    }
}