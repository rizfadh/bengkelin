package com.rizky.bengkelin.data.remote.response

import com.google.gson.annotations.SerializedName

data class OsrmResponse(

    @field:SerializedName("routes")
    val routes: List<RoutesResult>,

)

data class RoutesResult(

    @field:SerializedName("distance")
    val distance: Double,

)