package com.rizky.bengkelin.ui.login

import androidx.lifecycle.*
import com.google.gson.GsonBuilder
import com.rizky.bengkelin.data.remote.response.ApiResponse
import com.rizky.bengkelin.data.remote.response.LoginResult
import com.rizky.bengkelin.data.repository.UserRepository
import com.rizky.bengkelin.ui.common.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _loginResult = MutableLiveData<Result<LoginResult>>()
    val loginResult: LiveData<Result<LoginResult>> get() = _loginResult

    fun login(email: String, password: String) {
        _loginResult.value = Result.Loading
        viewModelScope.launch {
            try {
                val response = userRepository.login(email, password)
                if (response.isSuccessful) {
                    response.body()?.let {
                        it.loginResult?.let { result ->
                            _loginResult.value = Result.Success(result)
                        } ?: run { _loginResult.value = (Result.Empty) }
                    }
                } else {
                    val result = response.errorBody()?.string()
                    val error: ApiResponse = GsonBuilder().create()
                        .fromJson(result, ApiResponse::class.java)
                    _loginResult.value = Result.Error(error.message)
                }
            } catch (e: Exception) {
                _loginResult.value = Result.Error(e.message.toString())
            }
        }
    }

    fun saveUserData(token: String, name: String, email: String): LiveData<Result<Unit>> =
        liveData {
            emit(Result.Loading)
            userRepository.saveUserDataPreference(token, name, email)
            emit(Result.Success(Unit))
        }
}