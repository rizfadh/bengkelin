package com.rizky.bengkelin.data.repository

import com.rizky.bengkelin.data.preference.UserPreference
import com.rizky.bengkelin.data.remote.retrofit.ApiService

class UserRepository(
    private val preference: UserPreference,
    private val apiService: ApiService
) {

    fun getUserDataPreference() = preference.getUserData()

    suspend fun saveUserDataPreference(
        token: String,
        name: String,
        email: String
    ) = preference.saveUserData(token, name, email)

    suspend fun logout() = preference.deleteUserData()

    suspend fun login(
        email: String,
        password: String
    ) = apiService.login(email, password)

    suspend fun register(
        name: String,
        email: String,
        password: String
    ) = apiService.register(name, email, password)

    suspend fun getBengkelList(token: String) = apiService.getBengkelList(token, 1)
}