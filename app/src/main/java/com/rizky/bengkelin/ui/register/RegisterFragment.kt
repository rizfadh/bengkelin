package com.rizky.bengkelin.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.rizky.bengkelin.R
import com.rizky.bengkelin.databinding.FragmentRegisterBinding
import com.rizky.bengkelin.ui.common.Result
import com.rizky.bengkelin.ui.common.alert
import com.rizky.bengkelin.ui.login.LoginFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.registerResult.observe(viewLifecycleOwner) {
            when (it) {
                is Result.Loading -> {
                    showLoading(true)
                }
                is Result.Success -> {
                    alert(requireActivity(), getString(R.string.success), it.data.message)
                    parentFragmentManager.commit {
                        replace(R.id.fragmentContainer, LoginFragment())
                        setReorderingAllowed(true)
                    }
                }
                is Result.Error -> {
                    showLoading(false)
                    alert(requireActivity(), getString(R.string.error), it.error)
                }
                else -> {}
            }
        }

        binding.apply {
            btnLogin.setOnClickListener {
                parentFragmentManager.commit {
                    replace(R.id.fragmentContainer, LoginFragment())
                    setReorderingAllowed(true)
                }
            }

            btnRegister.setOnClickListener { register() }
        }
    }

    private fun register() {
        val name = binding.edName.text.toString().trim()
        val email = binding.edEmail.text.toString().trim()
        val password = binding.edPassword.text.toString().trim()
        val requiredText = getString(R.string.required)

        when {
            name.isEmpty() -> binding.tlName.error = requiredText
            email.isEmpty() -> binding.tlEmail.error = requiredText
            password.isEmpty() -> binding.tlPassword.error = requiredText
            else -> {
                viewModel.register(name, email, password)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.root.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}