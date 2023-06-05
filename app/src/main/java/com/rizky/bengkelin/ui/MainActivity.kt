package com.rizky.bengkelin.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.rizky.bengkelin.R
import com.rizky.bengkelin.databinding.ActivityMainBinding
import com.rizky.bengkelin.model.UserData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getUserDataPreference().observe(this) { userData ->
            userData.token?.let { initMain(userData) } ?: run { toAuthActivity() }
        }
    }

    private fun initMain(userData: UserData) {
        viewModel.setUserData(userData)
        supportActionBar?.show()
        binding.mainContainer.visibility = View.VISIBLE
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController.apply {
            val navGraph = navInflater.inflate(R.navigation.navigation_graph)
            graph = navGraph
        }
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.analysisFragment,
                R.id.profileFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.bottomNavigationView.setupWithNavController(navController)
    }

    private fun toAuthActivity() {
        val authIntent = Intent(this, AuthActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(authIntent)
    }
}