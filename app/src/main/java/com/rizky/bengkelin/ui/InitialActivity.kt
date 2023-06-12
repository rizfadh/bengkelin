package com.rizky.bengkelin.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.rizky.bengkelin.R
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
            viewModel.getUserToken().observe(this@InitialActivity) { userToken ->
                userToken?.let { toMainActivity(it) } ?: run { toAuthActivity() }
            }
        }
    }

    private fun toMainActivity(userToken: String) {
        val mainIntent = Intent(this, MainActivity::class.java).apply {
            putExtra(MainActivity.EXTRA_USER_TOKEN, userToken)
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