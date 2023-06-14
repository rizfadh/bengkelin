package com.rizky.bengkelin.ui.home

import android.annotation.SuppressLint
import android.app.Activity.RESULT_CANCELED
import android.content.IntentSender
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.rizky.bengkelin.R
import com.rizky.bengkelin.databinding.FragmentHomeBinding
import com.rizky.bengkelin.ui.MainViewModel
import com.rizky.bengkelin.ui.adapter.BengkelAdapter
import com.rizky.bengkelin.ui.common.Result
import com.rizky.bengkelin.ui.common.alert
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel: MainViewModel by activityViewModels()
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bengkelAdapter = BengkelAdapter { bengkelId ->
            HomeFragmentDirections.actionHomeFragmentToDetailFragment(bengkelId).let {
                findNavController().navigate(it)
            }
        }

        mainViewModel.bengkelList?.let {
            bengkelAdapter.submitList(it)
        } ?: run {
            getBengkelList(bengkelAdapter)
        }

        binding.rvBengkel.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            setHasFixedSize(false)
            adapter = bengkelAdapter
        }

        binding.swipeRefresh.apply {
            isRefreshing = false
            setOnRefreshListener {
                getBengkelList(bengkelAdapter)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.root.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.rvBengkel.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    private val resolutionLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == RESULT_CANCELED) {
            alert(
                requireActivity(),
                R.drawable.ic_error_24,
                getString(R.string.error),
                getString(R.string.gps_off_alert)
            )
        }
    }

    private fun getBengkelList(bengkelAdapter: BengkelAdapter) {

        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            TimeUnit.SECONDS.toMillis(1)
        ).apply { setWaitForAccurateLocation(true) }.build()

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        LocationServices.getSettingsClient(requireActivity()).apply {
            checkLocationSettings(builder.build()).addOnSuccessListener {
                setBengkelList(bengkelAdapter)
            }.addOnFailureListener {
                if (it is ResolvableApiException) {
                    try {
                        resolutionLauncher.launch(
                            IntentSenderRequest.Builder(it.resolution).build()
                        )
                        binding.swipeRefresh.isRefreshing = false
                    } catch (e: IntentSender.SendIntentException) {
                        alert(
                            requireActivity(),
                            R.drawable.ic_error_24,
                            getString(R.string.error),
                            e.message
                        )
                        binding.swipeRefresh.isRefreshing = false
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun setBengkelList(bengkelAdapter: BengkelAdapter) {
        LocationServices.getFusedLocationProviderClient(requireActivity()).apply {
            getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, object : CancellationToken() {
                override fun onCanceledRequested(
                    p0: OnTokenCanceledListener
                ) = CancellationTokenSource().token

                override fun isCancellationRequested() = false
            }).addOnSuccessListener {
                it?.let { location ->
                    viewModel.getBengkelList(location)
                        .observe(viewLifecycleOwner) { result ->
                            when (result) {
                                is Result.Loading -> showLoading(true)
                                is Result.Success -> {
                                    mainViewModel.setBengkelList(result.data)
                                    bengkelAdapter.submitList(result.data)
                                    showLoading(false)
                                    binding.swipeRefresh.isRefreshing = false
                                }
                                is Result.Empty -> {
                                    alert(
                                        requireActivity(),
                                        R.drawable.ic_error_24,
                                        getString(R.string.error),
                                        getString(R.string.empty)
                                    )
                                    binding.swipeRefresh.isRefreshing = false
                                }
                                is Result.Error -> {
                                    alert(
                                        requireActivity(),
                                        R.drawable.ic_error_24,
                                        getString(R.string.error),
                                        result.error
                                    )
                                    binding.swipeRefresh.isRefreshing = false
                                }
                            }
                        }
                } ?: run {
                    alert(
                        requireActivity(),
                        R.drawable.ic_error_24,
                        getString(R.string.error),
                        getString(R.string.location_not_found)
                    )
                }
            }
        }
    }
}