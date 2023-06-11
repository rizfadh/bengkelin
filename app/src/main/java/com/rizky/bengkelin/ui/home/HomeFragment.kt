package com.rizky.bengkelin.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.LocationServices
import com.rizky.bengkelin.R
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

        val bengkelAdapter = BengkelAdapter {
            findNavController().navigate(R.id.action_homeFragment_to_detailFragment)
        }

        LocationServices.getFusedLocationProviderClient(requireActivity()).apply {
            lastLocation.addOnSuccessListener {
                it?.let { location ->
                    val token = "Bearer ${userData.token}"
                    viewModel.getBengkelList(token, location)
                        .observe(viewLifecycleOwner) { result ->
                            when (result) {
                                is Result.Loading -> showLoading(true)
                                is Result.Success -> {
                                    bengkelAdapter.submitList(result.data)
                                    showLoading(false)
                                }
                                is Result.Empty -> alert(
                                    requireActivity(),
                                    getString(R.string.error),
                                    getString(R.string.empty)
                                )
                                is Result.Error -> alert(
                                    requireActivity(),
                                    getString(R.string.error),
                                    result.error
                                )
                            }
                        }
                }
            }
        }

        binding.rvBengkel.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            setHasFixedSize(false)
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