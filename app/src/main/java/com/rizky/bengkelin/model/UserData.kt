package com.rizky.bengkelin.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserData(
    val token: String? = null,
    val name: String? = null,
    val email: String? = null
): Parcelable