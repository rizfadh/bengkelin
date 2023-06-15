package com.rizky.bengkelin.ui.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.location.LocationServices
import com.rizky.bengkelin.R
import com.rizky.bengkelin.data.remote.response.BengkelResult
import com.rizky.bengkelin.data.remote.response.DetailResult
import com.rizky.bengkelin.data.remote.response.JasaResult
import com.rizky.bengkelin.databinding.FragmentDetailBinding
import com.rizky.bengkelin.databinding.ItemCheckboxBinding
import com.rizky.bengkelin.model.ServiceOrder
import com.rizky.bengkelin.ui.adapter.ReviewAdapter
import com.rizky.bengkelin.ui.common.Result
import com.rizky.bengkelin.ui.common.alert
import com.rizky.bengkelin.utils.formatToCurrency
import com.rizky.bengkelin.utils.formatToDistance
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailViewModel by viewModels()
    private val args: DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bengkelId = args.bengkelId
        LocationServices.getFusedLocationProviderClient(requireActivity()).apply {
            lastLocation.addOnSuccessListener {
                it?.let { location ->
                    viewModel.getBengkelDetail(location, bengkelId)
                        .observe(viewLifecycleOwner) { result ->
                            when (result) {
                                is Result.Loading -> showLoading(true)
                                is Result.Success -> {
                                    setDetailView(result.data)
                                    showLoading(false)
                                }
                                is Result.Error -> alert(
                                    requireActivity(),
                                    R.drawable.ic_error_24,
                                    getString(R.string.error),
                                    result.error
                                )
                                else -> {}
                            }
                        }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.root.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.layoutContainer.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    private fun setDetailView(data: DetailResult) {

        val selectedServices = mutableMapOf<Int, JasaResult>()

        Glide.with(requireActivity())
            .load(data.bengkel.photoURL)
            .apply(RequestOptions.placeholderOf(R.drawable.img_image_loading))
            .into(binding.ivPhoto)
        binding.tvName.text = data.bengkel.nama
        binding.tvAddress.text = data.bengkel.alamat
        binding.layoutInformation.apply {
            tvQueue.text = "0"
            tvDistance.text = data.bengkel.distance?.formatToDistance()
            tvRating.text = data.bengkel.totalNilaiJumlahReview.toString()
            tvOpen.text = data.bengkel.jamBuka
        }

        val vehicle = data.bengkel.kendaraan.split(";")
        binding.rbOne.text = vehicle[0]
        if (vehicle.size < 2) {
            binding.rbTwo.visibility = View.GONE
            binding.rbOne.isChecked = true
        } else binding.rbTwo.text = vehicle[1]

        data.jasas.forEach { jasa ->
            val cb = ItemCheckboxBinding.inflate(
                LayoutInflater.from(requireActivity()),
                binding.layoutService,
                true
            )

            cb.apply {
                tvCbName.text = jasa.nama
                tvCbDescription.text = jasa.keterangan
                tvCbPrice.text = jasa.harga.formatToCurrency()
                root.setOnClickListener {
                    root.isChecked = !root.isChecked
                    if (root.isChecked) selectedServices[jasa.id] = jasa
                    else selectedServices.remove(jasa.id)
                }
            }
        }

        binding.tvReview.text = data.bengkel.totalNilaiJumlahReview.toString()
        ReviewAdapter().also { reviewAdapter ->
            reviewAdapter.submitList(data.reviews)
            binding.rvReview.apply {
                layoutManager = LinearLayoutManager(requireActivity())
                setHasFixedSize(false)
                adapter = reviewAdapter
            }
        }

        binding.btnConfirm.setOnClickListener { confirmServices(data.bengkel, selectedServices) }
    }

    private fun confirmServices(
        bengkel: BengkelResult,
        selectedServices: Map<Int, JasaResult>
    ) {
        when {
            binding.rgVehicle.checkedRadioButtonId == -1 -> alert(
                requireActivity(),
                R.drawable.ic_hold_24,
                getString(R.string.hold_on),
                getString(R.string.vehicle_empty)
            )
            selectedServices.isEmpty() -> alert(
                requireActivity(),
                R.drawable.ic_hold_24,
                getString(R.string.hold_on),
                getString(R.string.services_empty)
            )
            else -> {
                val selectedVehicle = binding.rgVehicle.checkedRadioButtonId.let {
                    if (binding.rbOne.id == it) binding.rbOne.text
                    else binding.rbTwo.text
                }.toString()
                val selectedServicesList = selectedServices.values.toList()
                val serviceOrder = ServiceOrder(
                    bengkel,
                    selectedVehicle,
                    selectedServicesList,
                    binding.edNote.text.toString().trim()
                )
                DetailFragmentDirections.actionDetailFragmentToConfirmationFragment(
                    serviceOrder
                ).let {
                    findNavController().navigate(it)
                }
            }
        }
    }
}