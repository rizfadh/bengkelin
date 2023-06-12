package com.rizky.bengkelin.data.repository

import com.rizky.bengkelin.data.preference.UserPreference
import com.rizky.bengkelin.data.remote.retrofit.BengkelinApiService
import com.rizky.bengkelin.data.remote.retrofit.PredictionApiService
import okhttp3.MultipartBody

class UserRepository(
    private val preference: UserPreference,
    private val bengkelinApiService: BengkelinApiService,
    private val predictionApiService: PredictionApiService
) {

    fun getUserTokenPreference() = preference.getUserToken()

    suspend fun saveUserTokenPreference(token: String) = preference.saveUserToken(token)

    suspend fun deleteUserTokenPreference() = preference.deleteUserToken()

    suspend fun login(
        email: String,
        password: String
    ) = bengkelinApiService.login(email, password)

    suspend fun register(
        username: String,
        email: String,
        password: String
    ) = bengkelinApiService.register(username, email, password)

    suspend fun getBengkelList() = bengkelinApiService.getBengkelList()

    suspend fun getBengkelDetail(
        bengkelId: Int
    ) = bengkelinApiService.getBengkelDetail(bengkelId)

    suspend fun getPrediction(file: MultipartBody.Part) = predictionApiService.getPrediction(file)
}