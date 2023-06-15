package com.rizky.bengkelin.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.rizky.bengkelin.R
import com.rizky.bengkelin.data.remote.response.ProfileResult
import com.rizky.bengkelin.databinding.FragmentProfileBinding
import com.rizky.bengkelin.ui.AuthActivity
import com.rizky.bengkelin.ui.MainViewModel
import com.rizky.bengkelin.ui.common.Result
import com.rizky.bengkelin.ui.common.alert
import com.rizky.bengkelin.utils.formatToDate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel: MainViewModel by activityViewModels()
    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var userToken: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userToken = mainViewModel.userToken

        val token = "Bearer $userToken"
        viewModel.getUserProfile(token).observe(viewLifecycleOwner) {
            when (it) {
                is Result.Loading -> {
                    showLoading(true)
                }
                is Result.Success -> {
                    showLoading(false)
                    setProfileView(it.data)
                }
                is Result.Empty -> alert(
                    requireActivity(),
                    R.drawable.ic_error_24,
                    getString(R.string.error),
                    getString(R.string.empty)
                )
                is Result.Error -> {
                    alert(
                        requireActivity(),
                        R.drawable.ic_error_24,
                        getString(R.string.error),
                        it.error
                    )
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

    private fun setProfileView(data: ProfileResult) {
        binding.apply {
            tvUserName.text = data.username
            tvUserEmail.text = data.email
            tvUserCreatedAt.text = getString(R.string.created_at, data.createdAt.formatToDate())
            btnLogout.setOnClickListener {
                viewModel.logout()
                toAuthActivity()
            }
        }
    }

    private fun toAuthActivity() {
        val authIntent = Intent(requireActivity(), AuthActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(authIntent)
    }
}