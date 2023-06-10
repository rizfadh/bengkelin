package com.rizky.bengkelin.ui

import android.annotation.SuppressLint
import android.location.Location
import androidx.lifecycle.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import com.google.gson.GsonBuilder
import com.google.maps.android.SphericalUtil
import com.rizky.bengkelin.data.remote.response.ApiResponse
import com.rizky.bengkelin.data.remote.response.BengkelResult
import com.rizky.bengkelin.data.repository.UserRepository
import com.rizky.bengkelin.model.UserData
import com.rizky.bengkelin.ui.common.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val fusedLocation: FusedLocationProviderClient
) : ViewModel() {

    lateinit var userData: UserData
        private set

    private val _bengkelList = MutableLiveData<Result<List<BengkelResult>>>()
    val bengkelList: LiveData<Result<List<BengkelResult>>> = _bengkelList

    fun setUserData(data: UserData) = run { userData = data }

    @SuppressLint("MissingPermission")
    fun getBengkelList(token: String) {
        _bengkelList.value = Result.Loading
        viewModelScope.launch {
            try {
                val response = userRepository.getBengkelList(token)
                if (response.isSuccessful) {
                    response.body()?.let {
                        it.bengkelList?.let { result ->
                            fusedLocation.lastLocation.addOnSuccessListener { loc ->
                                loc?.let { location ->
                                    val sorted = sortBengkel(result, location)
                                    _bengkelList.value = Result.Success(sorted)
                                }
                            }
                        } ?: run { _bengkelList.value = Result.Empty }
                    }
                } else {
                    val result = response.errorBody()?.string()
                    val error: ApiResponse = GsonBuilder().create()
                        .fromJson(result, ApiResponse::class.java)
                    _bengkelList.value = Result.Error(error.message)
                }
            } catch (e: Exception) {
                _bengkelList.value = Result.Error(e.message.toString())
            }
        }
    }

    private fun sortBengkel(
        list: List<BengkelResult>,
        location: Location
    ) = list.map { result ->
        val userLocation = LatLng(location.latitude, location.longitude)
        val bengkelLocation = LatLng(result.lat, result.lon)
        result.distance = SphericalUtil.computeDistanceBetween(
            userLocation, bengkelLocation
        ).toInt()
        result
    }.sortedBy { result -> result.distance }
}