package com.rizky.bengkelin.model

import android.os.Parcelable
import com.rizky.bengkelin.data.remote.response.BengkelResult
import com.rizky.bengkelin.data.remote.response.JasaResult
import kotlinx.parcelize.Parcelize

@Parcelize
data class ServiceOrder(
    var bengkel: BengkelResult? = null,
    var vehicle: String? = null,
    var service: List<JasaResult>? = null,
    var note: String? = null
) : Parcelable