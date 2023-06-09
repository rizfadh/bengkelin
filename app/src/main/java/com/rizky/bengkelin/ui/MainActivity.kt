package com.rizky.bengkelin.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.rizky.bengkelin.R
import com.rizky.bengkelin.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()

    private lateinit var navController: NavController

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.getStringExtra(EXTRA_USER_TOKEN)?.let {
            viewModel.setUserToken(it)
        }

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.navHostFragment) as NavHostFragment

        val setOfMenu = setOf(
            R.id.homeFragment,
            R.id.analysisFragment,
            R.id.profileFragment
        )

        navController = navHostFragment.navController.apply {
            val navGraph = navInflater.inflate(R.navigation.navigation_graph)
            graph = navGraph
            addOnDestinationChangedListener { _, dest, _ ->
                if (dest.id in setOfMenu) binding.bottomNavigationView.visibility = View.VISIBLE
                else binding.bottomNavigationView.visibility = View.GONE
            }
        }

        val appBarConfiguration = AppBarConfiguration(setOfMenu)

        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.bottomNavigationView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val EXTRA_USER_TOKEN = "extra_user_token"
    }
}