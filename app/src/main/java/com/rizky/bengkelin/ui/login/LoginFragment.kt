package com.rizky.bengkelin.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.rizky.bengkelin.R
import com.rizky.bengkelin.databinding.FragmentLoginBinding
import com.rizky.bengkelin.ui.MainActivity
import com.rizky.bengkelin.ui.common.Result
import com.rizky.bengkelin.ui.common.alert
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loginResult.observe(viewLifecycleOwner) {
            when (it) {
                is Result.Loading -> showLoading(true)
                is Result.Success -> {
                    viewModel.saveUserToken(it.data)
                        .observe(viewLifecycleOwner) { result ->
                            if (result is Result.Success) {
                                toMainActivity(it.data)
                            }
                        }
                }
                is Result.Error -> {
                    showLoading(false)
                    alert(
                        requireActivity(),
                        R.drawable.ic_error_24,
                        getString(R.string.error),
                        it.error
                    )
                }
                else -> {}
            }
        }

        binding.apply {
            btnRegister.setOnClickListener {
                findNavController().navigate(
                    R.id.action_loginFragment_to_registerFragment
                )
            }

            btnLogin.setOnClickListener { login() }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun login() {
        val email = binding.edEmail.text.toString().trim()
        val password = binding.edPassword.text.toString().trim()
        val requiredText = getString(R.string.required)

        when {
            email.isEmpty() -> binding.tlEmail.error = requiredText
            password.isEmpty() -> binding.tlPassword.error = requiredText
            else -> {
                viewModel.login(email, password)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.root.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun toMainActivity(userToken: String) {
        val mainIntent = Intent(requireActivity(), MainActivity::class.java).apply {
            putExtra(MainActivity.EXTRA_USER_TOKEN, userToken)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(mainIntent)
    }
}