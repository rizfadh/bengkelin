package com.rizky.bengkelin.ui.home

import androidx.lifecycle.ViewModel
import com.rizky.bengkelin.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

}