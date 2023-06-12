package com.rizky.bengkelin.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.google.gson.GsonBuilder
import com.rizky.bengkelin.data.remote.response.CommonResponse
import com.rizky.bengkelin.data.remote.response.DetailResult
import com.rizky.bengkelin.data.repository.UserRepository
import com.rizky.bengkelin.ui.common.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    fun getBengkelDetail(bengkelId: Int): LiveData<Result<DetailResult>> = liveData {
        emit(Result.Loading)
        try {
            val response = userRepository.getBengkelDetail(bengkelId)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(Result.Success(it.detail))
                } ?: run { emit(Result.Empty) }
            } else {
                val result = response.errorBody()?.string()
                val error: CommonResponse = GsonBuilder().create()
                    .fromJson(result, CommonResponse::class.java)
                emit(Result.Error(error.message))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

}