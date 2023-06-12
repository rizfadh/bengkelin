package com.rizky.bengkelin.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.rizky.bengkelin.R
import com.rizky.bengkelin.data.remote.response.DetailResult
import com.rizky.bengkelin.databinding.FragmentDetailBinding
import com.rizky.bengkelin.ui.common.Result
import com.rizky.bengkelin.ui.common.alert
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bengkelId = args.bengkelId
        viewModel.getBengkelDetail(bengkelId).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> showLoading(true)
                is Result.Success -> {
                    setDetailView(result.data)
                    showLoading(false)
                }
                is Result.Empty -> alert(
                    requireActivity(),
                    R.drawable.ic_error_24,
                    getString(R.string.error),
                    getString(R.string.empty)
                )
                is Result.Error -> alert(
                    requireActivity(),
                    R.drawable.ic_error_24,
                    getString(R.string.error),
                    result.error
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(isLoading: Boolean) {
        println(isLoading)
    }

    private fun setDetailView(data: DetailResult) {
        Glide.with(requireActivity())
            .load(data.bengkel.photoUrl)
            .apply(RequestOptions.placeholderOf(R.drawable.img_image_loading))
            .into(binding.ivPhoto)
        binding.tvName.text = data.bengkel.nama
        binding.tvAddress.text = data.bengkel.alamat
    }
}