package com.rizky.bengkelin.ui.home

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.google.gson.GsonBuilder
import com.rizky.bengkelin.data.remote.response.BengkelResult
import com.rizky.bengkelin.data.remote.response.CommonResponse
import com.rizky.bengkelin.data.repository.UserRepository
import com.rizky.bengkelin.ui.common.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    fun getBengkelList(
        userLoc: Location
    ): LiveData<Result<List<BengkelResult>>> = liveData {
        emit(Result.Loading)
        try {
            val bengkelResult = userRepository.getBengkelList()
            val result = bengkelResult.body()?.bengkelList?.map {
                val osrmResult = userRepository.calculateDistance(
                    userLoc.longitude,
                    userLoc.latitude,
                    it.longitude,
                    it.latitude
                )
                it.distance = osrmResult.body()?.routes?.get(0)?.distance?.toInt() ?: 0
                it
            }
            result?.let {
                emit(Result.Success(it))
            }
            if (!bengkelResult.isSuccessful) {
                val error = bengkelResult.errorBody()?.string()
                val errorMsg: CommonResponse = GsonBuilder().create()
                    .fromJson(error, CommonResponse::class.java)
                emit(Result.Error(errorMsg.message))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }
}