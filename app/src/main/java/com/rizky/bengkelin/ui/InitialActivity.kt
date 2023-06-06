package com.rizky.bengkelin.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.rizky.bengkelin.R
import com.rizky.bengkelin.model.UserData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class InitialActivity : AppCompatActivity() {

    private val viewModel: InitialViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_initial)

        lifecycleScope.launch {
            delay(2000)
            viewModel.getUserDataPreference().observe(this@InitialActivity) { userData ->
                userData.token?.let { toMainActivity(userData) } ?: run { toAuthActivity() }
            }
        }
    }

    private fun toMainActivity(userData: UserData) {
        val mainIntent = Intent(this, MainActivity::class.java).apply {
            putExtra(MainActivity.EXTRA_USER_DATA, userData)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(mainIntent)
    }

    private fun toAuthActivity() {
        val authIntent = Intent(this, AuthActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(authIntent)
    }
}