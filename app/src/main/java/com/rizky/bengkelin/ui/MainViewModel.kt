package com.rizky.bengkelin.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.rizky.bengkelin.model.UserData
import com.rizky.bengkelin.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    lateinit var userData: UserData
        private set

    fun setUserData(data: UserData) = run { userData = data}

    fun getUserDataPreference() = userRepository.getUserDataPreference().asLiveData()
}