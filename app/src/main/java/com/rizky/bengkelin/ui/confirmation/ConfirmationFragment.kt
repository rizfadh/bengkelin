package com.rizky.bengkelin.ui.confirmation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.rizky.bengkelin.R
import com.rizky.bengkelin.databinding.FragmentConfirmationBinding
import com.rizky.bengkelin.databinding.ItemConfirmationServiceBinding
import com.rizky.bengkelin.ui.common.alert
import com.rizky.bengkelin.utils.formatToCurrency

class ConfirmationFragment : Fragment() {

    private var _binding: FragmentConfirmationBinding? = null
    private val binding get() = _binding!!

    private val args: ConfirmationFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConfirmationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val serviceOrder = args.serviceOrder
        val (bengkel, vehicle, service, note) = serviceOrder
        val discount = 3000

        val subtotalPayment = service?.sumOf {
            it.harga
        } as Int
        val totalPayment = (subtotalPayment - discount)

        binding.apply {
            tvBengkelName.text = bengkel?.nama
            tvBengkelAddress.text = bengkel?.alamat
            tvVehicle.text = vehicle

            if (note.isNullOrEmpty()) layoutNote.visibility = View.GONE
            else tvNote.text = note

            tvSubtotalPayment.text = subtotalPayment.formatToCurrency()
            tvDiscount.text = discount.formatToCurrency()
            tvTotalPaymentSummary.text = totalPayment.formatToCurrency()
            tvTotalPayment.text = totalPayment.formatToCurrency()

            btnProcess.setOnClickListener { processOrder() }
        }

        service.forEach { jasa ->
            val tv = ItemConfirmationServiceBinding.inflate(
                LayoutInflater.from(requireActivity()),
                binding.layoutService,
                true
            )

            tv.apply {
                tvName.text = jasa.nama
                tvDescription.text = jasa.keterangan
                tvPrice.text = jasa.harga.formatToCurrency()
            }
        }
    }

    private fun processOrder() {
        when {
            binding.rgPayment.checkedRadioButtonId == -1 -> alert(
                requireActivity(),
                R.drawable.ic_hold_24,
                getString(R.string.hold_on),
                getString(R.string.payment_empty)
            )
            else -> {
                alert(
                    requireActivity(),
                    R.drawable.ic_success_24,
                    getString(R.string.success),
                    getString(R.string.booking_success)
                ) {
                    findNavController().navigate(R.id.action_confirmationFragment_to_homeFragment)
                }
            }
        }
    }
}