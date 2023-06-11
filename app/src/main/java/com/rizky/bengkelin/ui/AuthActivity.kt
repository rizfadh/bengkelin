package com.rizky.bengkelin.ui

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rizky.bengkelin.R
import com.rizky.bengkelin.ui.common.alert
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_auth)

        if (!hasPermission()) requestPermissions()
    }

    private fun hasPermission() = EasyPermissions.hasPermissions(
        this,
        CAMERA_PERMISSION,
        FINE_LOCATION_PERMISSION,
        COARSE_LOCATION_PERMISSION
    )

    private fun requestPermissions() {
        EasyPermissions.requestPermissions(
            this,
            getString(R.string.permission_required),
            PERMISSION_REQUEST_CODE,
            CAMERA_PERMISSION,
            FINE_LOCATION_PERMISSION,
            COARSE_LOCATION_PERMISSION
        )
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        alert(
            this,
            R.drawable.ic_success_24,
            getString(R.string.success),
            getString(R.string.permissions_success)
        )
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
        private const val PERMISSION_REQUEST_CODE = 10
        private const val CAMERA_PERMISSION = Manifest.permission.CAMERA
        private const val FINE_LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION
        private const val COARSE_LOCATION_PERMISSION = Manifest.permission.ACCESS_COARSE_LOCATION
    }
}