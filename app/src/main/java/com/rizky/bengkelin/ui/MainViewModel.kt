package com.rizky.bengkelin.ui

import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {

    lateinit var userToken: String
        private set

    fun setUserToken(userToken: String) = run { this.userToken = userToken }
}