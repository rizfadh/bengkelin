package com.rizky.bengkelin.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizky.bengkelin.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }
}