package com.bot.alvinbot


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object AskPermission {

     const val PERMISSION_CODE = 10001

    const val CAMERA = Manifest.permission.CAMERA

    const val READ_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE

    const val WRITE_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE

    const val LOCATION_COARSE = Manifest.permission.ACCESS_COARSE_LOCATION

    const val LOCATION_FINE = Manifest.permission.ACCESS_FINE_LOCATION

    const val READ_CONTACTS = Manifest.permission.READ_CONTACTS

    const val CALL_PHONE = Manifest.permission.CALL_PHONE

    const val READ_SMS = Manifest.permission.READ_SMS


    const val ALREADY_PERMISSION_DENY_TEXT =
        """You have previously declined this permission.You must approve this permission in the app settings on your device."""

    val BOTH_CAMERA_AND_STORAGE = arrayOf(
        CAMERA,
        READ_STORAGE,
        WRITE_STORAGE
    )

    val BOTH_LOCATION = arrayOf(
        LOCATION_COARSE,
        LOCATION_FINE
    )

    fun checkPermission(context: Context, permission: Array<String>): Boolean {
        var count = 0
        for (i in permission) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    i
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                count++
            }
        }

        return count == permission.size


    }

    fun requestPermission(context: Context, permission: Array<out String>) {

        ActivityCompat.requestPermissions(
            context as Activity,
            permission,
            PERMISSION_CODE
        )

    }


    fun handlePermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ): Boolean {
        return when (requestCode) {
            PERMISSION_CODE -> {
                var count = 0
                for (i in permissions.indices) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED
                    ) {
                        count++
                    }
                }
                return count == permissions.size

            }
            else -> {
                handlePermissionsResult(
                    requestCode,
                    permissions,
                    grantResults
                )
            }
        }
    }

    fun shouldShowRequestPermissionRationale(
        context: Context,
        permissions: Array<out String>
    ): Boolean {
        var count = 0
        for (i in permissions.indices) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    context as Activity,
                    permissions[i]
                )
            ) {
                count++
            }
        }
        return count == permissions.size
    }

    fun launchSettingsScreen(context: Context) {
        context.startActivity(
            Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + BuildConfig.APPLICATION_ID)
            )
        )
    }

    fun checkVersion(): Boolean {
        return VersionUtils.isGreaterThanM()
    }
}