package com.rizky.bengkelin.ui

import androidx.lifecycle.ViewModel
import com.rizky.bengkelin.model.UserData

class MainViewModel() : ViewModel() {

    lateinit var userData: UserData
        private set

    fun setUserData(data: UserData) = run { userData = data }
}