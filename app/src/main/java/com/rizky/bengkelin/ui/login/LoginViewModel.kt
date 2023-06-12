package com.rizky.bengkelin.ui.login

import androidx.lifecycle.*
import com.google.gson.GsonBuilder
import com.rizky.bengkelin.data.remote.response.CommonResponse
import com.rizky.bengkelin.data.repository.UserRepository
import com.rizky.bengkelin.ui.common.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _loginResult = MutableLiveData<Result<String>>()
    val loginResult: LiveData<Result<String>> get() = _loginResult

    fun login(email: String, password: String) {
        _loginResult.value = Result.Loading
        viewModelScope.launch {
            try {
                val response = userRepository.login(email, password)
                if (response.isSuccessful) {
                    response.body()?.let { result ->
                        _loginResult.value = Result.Success(result.token)
                    }
                } else {
                    val result = response.errorBody()?.string()
                    val error: CommonResponse = GsonBuilder().create()
                        .fromJson(result, CommonResponse::class.java)
                    _loginResult.value = Result.Error(error.message)
                }
            } catch (e: Exception) {
                _loginResult.value = Result.Error(e.message.toString())
            }
        }
    }

    fun saveUserToken(
        userToken: String
    ): LiveData<Result<Unit>> = liveData {
        emit(Result.Loading)
        userRepository.saveUserTokenPreference(userToken)
        emit(Result.Success(Unit))
    }
}