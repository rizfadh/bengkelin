package com.rizky.bengkelin.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import com.rizky.bengkelin.databinding.FragmentHomeBinding
import com.rizky.bengkelin.model.UserData
import com.rizky.bengkelin.ui.MainViewModel
import com.rizky.bengkelin.ui.adapter.BengkelAdapter
import com.rizky.bengkelin.ui.common.Result
import com.rizky.bengkelin.ui.common.alert
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel: MainViewModel by activityViewModels()
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var userData: UserData

    private lateinit var fusedLocation: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocation = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userData = mainViewModel.userData

        val bengkelAdapter = BengkelAdapter {}
        val token = "Bearer ${userData.token}"
        viewModel.getBengkelList(token).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> showLoading(true)
                is Result.Success -> {
                    showLoading(false)
                    fusedLocation.lastLocation.addOnSuccessListener {
                        it?.let { location ->
                            println(location.latitude)
                            val bengkelList = result.data.map { bengkelResult ->
                                val userLocation = LatLng(location.latitude, location.longitude)
                                val bengkelLocation = LatLng(bengkelResult.lat, bengkelResult.lon)
                                bengkelResult.distance = SphericalUtil.computeDistanceBetween(
                                    userLocation, bengkelLocation
                                ).toInt()
                                bengkelResult
                            }.sortedBy { bengkelResult -> bengkelResult.distance }
                            bengkelAdapter.submitList(bengkelList)
                        }
                    }
                }
                is Result.Empty -> {
                    showLoading(false)
                    alert(requireActivity(), "error", "HAH KSOONG")
                }
                is Result.Error -> {
                    showLoading(false)
                    alert(requireActivity(), "error", result.error)
                }
            }
        }

        binding.rvBengkel.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            setHasFixedSize(true)
            adapter = bengkelAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.root.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}