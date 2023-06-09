package com.rizky.bengkelin.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.google.gson.GsonBuilder
import com.rizky.bengkelin.data.remote.response.ApiResponse
import com.rizky.bengkelin.data.remote.response.BengkelResult
import com.rizky.bengkelin.data.repository.UserRepository
import com.rizky.bengkelin.ui.common.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    fun getBengkelList(token: String): LiveData<Result<List<BengkelResult>>> = liveData {
        emit(Result.Loading)
        try {
            val response = userRepository.getBengkelList(token)
            if (response.isSuccessful) {
                response.body()?.let {
                    it.bengkelList?.let { result ->
                        emit(Result.Success(result))
                    } ?: run { emit(Result.Empty) }
                }
            } else {
                val result = response.errorBody()?.string()
                val error: ApiResponse = GsonBuilder().create()
                    .fromJson(result, ApiResponse::class.java)
                emit(Result.Error(error.message))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }
}