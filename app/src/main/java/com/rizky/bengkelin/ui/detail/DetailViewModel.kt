package com.rizky.bengkelin.ui.detail

import android.location.Location
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

    fun getBengkelDetail(
        userLoc: Location,
        bengkelId: Int
    ): LiveData<Result<DetailResult>> = liveData {
        emit(Result.Loading)
        try {
            val bengkelResponse = userRepository.getBengkelDetail(bengkelId)
            val result = bengkelResponse.body()?.detail?.let { detail ->
                detail.bengkel.let {
                    val osrmResponse = userRepository.calculateDistance(
                        userLoc.longitude,
                        userLoc.latitude,
                        it.longitude,
                        it.latitude
                    )
                    it.distance = osrmResponse.body()?.routes?.get(0)?.distance ?: -1.0
                    it
                }
                detail
            }
            result?.let {
                emit(Result.Success(it))
            }
            if (!bengkelResponse.isSuccessful) {
                val error = bengkelResponse.errorBody()?.string()
                val errorMsg: CommonResponse = GsonBuilder().create()
                    .fromJson(error, CommonResponse::class.java)
                emit(Result.Error(errorMsg.message))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

}