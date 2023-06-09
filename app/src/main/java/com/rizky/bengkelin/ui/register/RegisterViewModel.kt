package com.rizky.bengkelin.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import com.rizky.bengkelin.data.remote.response.CommonResponse
import com.rizky.bengkelin.data.repository.UserRepository
import com.rizky.bengkelin.ui.common.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _registerResult = MutableLiveData<Result<String>>()
    val registerResult: LiveData<Result<String>> get() = _registerResult

    fun register(name: String, email: String, password: String) {
        _registerResult.value = Result.Loading
        viewModelScope.launch {
            try {
                val response = userRepository.register(name, email, password)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _registerResult.value = Result.Success(it.message)
                    }
                } else {
                    val result = response.errorBody()?.string()
                    val error: CommonResponse = GsonBuilder().create()
                        .fromJson(result, CommonResponse::class.java)
                    _registerResult.value = Result.Error(error.message)
                }
            } catch (e: Exception) {
                _registerResult.value = Result.Error(e.message.toString())
            }
        }
    }
}