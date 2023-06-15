package com.rizky.bengkelin.ui.analysis

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.google.gson.GsonBuilder
import com.rizky.bengkelin.data.remote.response.CommonResponse
import com.rizky.bengkelin.data.remote.response.PredictionResponse
import com.rizky.bengkelin.data.repository.UserRepository
import com.rizky.bengkelin.ui.common.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class AnalysisViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    fun getPrediction(
        file: MultipartBody.Part
    ): LiveData<Result<PredictionResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = userRepository.getPrediction(file)
            if (response.isSuccessful) {
                response.body()?.let {
                    if (it.prediction.isNotEmpty()) emit(Result.Success(it))
                    else emit(Result.Empty)
                }
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