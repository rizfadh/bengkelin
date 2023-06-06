package com.rizky.bengkelin.ui

import android.Manifest
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
import com.rizky.bengkelin.ui.common.alert
import com.rizky.bengkelin.utils.parcelable
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.parcelable<UserData>(EXTRA_USER_DATA)?.let {
            viewModel.setUserData(it)
        }

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

    private fun initMain(userData: UserData) {
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun hasPermission() = EasyPermissions.hasPermissions(
        this, CAMERA_PERMISSION, FINE_LOCATION_PERMISSION, COARSE_LOCATION_PERMISSION
    )

    private fun requestPermissions() {
        EasyPermissions.requestPermissions(
            this,
            getString(R.string.permission_required),
            PERMISSION_REQUEST_CODE,
            CAMERA_PERMISSION, FINE_LOCATION_PERMISSION, COARSE_LOCATION_PERMISSION
        )
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        alert(this, getString(R.string.success), getString(R.string.permissions_success))
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            SettingsDialog.Builder(this).build().show()
        } else requestPermissions()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    companion object {
        const val EXTRA_USER_DATA = "extra_user_data"

        const val PERMISSION_REQUEST_CODE = 10
        const val CAMERA_PERMISSION = Manifest.permission.CAMERA
        const val FINE_LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION
        const val COARSE_LOCATION_PERMISSION = Manifest.permission.ACCESS_COARSE_LOCATION
    }
}