package com.rizky.bengkelin.ui

import androidx.lifecycle.ViewModel
import com.rizky.bengkelin.data.remote.response.BengkelResult

class MainViewModel : ViewModel() {

    lateinit var userToken: String
        private set

    var bengkelList: List<BengkelResult>? = null
        private set

    fun setUserToken(userToken: String) = run { this.userToken = userToken }

    fun setBengkelList(bengkelList: List<BengkelResult>) = run { this.bengkelList = bengkelList }
}