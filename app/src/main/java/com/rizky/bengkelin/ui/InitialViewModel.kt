package com.rizky.bengkelin.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.rizky.bengkelin.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InitialViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    fun getUserToken() = userRepository.getUserTokenPreference().asLiveData()
}