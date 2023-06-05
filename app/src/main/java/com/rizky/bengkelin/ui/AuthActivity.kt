package com.rizky.bengkelin.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import com.rizky.bengkelin.R
import com.rizky.bengkelin.databinding.ActivityAuthBinding
import com.rizky.bengkelin.ui.login.LoginFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    private var _binding: ActivityAuthBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        _binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.fragmentContainer, LoginFragment())
                setReorderingAllowed(true)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}