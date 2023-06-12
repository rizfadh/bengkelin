package com.rizky.bengkelin.data.remote.response

import com.google.gson.annotations.SerializedName

data class PredictionResponse(

    @field:SerializedName("isTireGood")
    val isTireGood: String,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("prediction")
    val prediction: String,

)