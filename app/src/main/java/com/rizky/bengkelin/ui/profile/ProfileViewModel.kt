package com.rizky.bengkelin.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import com.rizky.bengkelin.data.remote.response.CommonResponse
import com.rizky.bengkelin.data.remote.response.ProfileResult
import com.rizky.bengkelin.data.repository.UserRepository
import com.rizky.bengkelin.ui.common.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {


    fun getUserProfile(token: String): LiveData<Result<ProfileResult>> = liveData {
        emit(Result.Loading)
        try {
            val response = userRepository.getUserProfile(token)
            if (response.isSuccessful) {
                response.body()?.let {
                    if (it.data.email.isNotEmpty()) emit(Result.Success(it.data))
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

    fun logout() {
        viewModelScope.launch {
            userRepository.deleteUserTokenPreference()
        }
    }
}