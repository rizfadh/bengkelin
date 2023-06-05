package com.rizky.bengkelin.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.rizky.bengkelin.model.UserData
import com.rizky.bengkelin.databinding.FragmentProfileBinding
import com.rizky.bengkelin.ui.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel: MainViewModel by activityViewModels()
    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var userData: UserData

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userData = mainViewModel.userData
        binding.apply {
            tvUserName.text = userData.name
            tvUserEmail.text = userData.email
        }
        binding.btnLogout.setOnClickListener { viewModel.logout() }
    }
}