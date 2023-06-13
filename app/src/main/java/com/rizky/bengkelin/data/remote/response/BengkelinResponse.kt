package com.rizky.bengkelin.data.remote.response

import com.google.gson.annotations.SerializedName

data class CommonResponse(

    @field:SerializedName("message")
    val message: String,

)

data class LoginResponse(

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("token")
    val token: String,

)

data class BengkelListResponse(

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val bengkelList: List<BengkelResult>
)

data class BengkelResult(

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("nama")
    val nama: String,

    @field:SerializedName("photoURL")
    val photoURL: String,

    @field:SerializedName("kendaraan")
    val kendaraan: String,

    @field:SerializedName("latitude")
    val latitude: Double,

    @field:SerializedName("longitude")
    val longitude: Double,

    @field:SerializedName("alamat")
    val alamat: String,

    @field:SerializedName("antrian")
    val antrian: Int,

    @field:SerializedName("totalNilaiJumlahReview")
    val totalNilaiJumlahReview: Double,

    @field:SerializedName("hariBuka")
    val hariBuka: String,

    @field:SerializedName("jamBuka")
    val jamBuka: String,

    var distance: Double
)

data class DetailResponse(

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val detail: DetailResult
)

data class DetailResult(
    @field:SerializedName("bengkel")
    val bengkel: BengkelResult,

    @field:SerializedName("jasas")
    val jasas: List<JasaResult>,

    @field:SerializedName("reviews")
    val reviews: List<ReviewResult>,
)

data class ReviewResult(

    @field:SerializedName("namaUser")
    val namaUser: String,

    @field:SerializedName("photoURL")
    val photoURL: String,

    @field:SerializedName("createdAt")
    val string: String,

    @field:SerializedName("rating")
    val rating: Double,

    @field:SerializedName("isiReview")
    val isiReview: String,
)

data class JasaResult(

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("nama")
    val nama: String,

    @field:SerializedName("keterangan")
    val keterangan: String,

    @field:SerializedName("harga")
    val harga: Int,
)