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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bengkelAdapter = BengkelAdapter { bengkelId ->
            HomeFragmentDirections.actionHomeFragmentToDetailFragment(bengkelId).apply {
                findNavController().navigate(this)
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

        binding.swipeRefresh.setOnRefreshListener {
            getBengkelList(bengkelAdapter)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.root.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    @SuppressLint("MissingPermission")
    private fun getBengkelList(bengkelAdapter: BengkelAdapter) {
        LocationServices.getFusedLocationProviderClient(requireActivity()).apply {
            lastLocation.addOnSuccessListener {
                it?.let { location ->
                    viewModel.getBengkelList(location)
                        .observe(viewLifecycleOwner) { result ->
                            when (result) {
                                is Result.Loading -> {
                                    binding.rvBengkel.visibility = View.GONE
                                    showLoading(true)
                                }
                                is Result.Success -> {
                                    mainViewModel.setBengkelList(result.data)
                                    bengkelAdapter.submitList(result.data)
                                    binding.rvBengkel.visibility = View.VISIBLE
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
                }
            }
        }
    }
}