package com.example.wearnn.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionUtils {
    const val REQUEST_ACTIVITY_RECOGNITION = 1
    const val REQUEST_BODY_SENSORS = 2
    const val REQUEST_PERMISSIONS_CODE = 100

    fun checkActivityRecognitionPermission(activity: Activity): Boolean =
        ContextCompat.checkSelfPermission(activity, Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_GRANTED

    fun requestActivityRecognitionPermission(activity: Activity) {
        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACTIVITY_RECOGNITION), REQUEST_ACTIVITY_RECOGNITION)
    }

    fun checkBodySensorsPermission(activity: Activity): Boolean =
        ContextCompat.checkSelfPermission(activity, Manifest.permission.BODY_SENSORS) == PackageManager.PERMISSION_GRANTED

    fun requestBodySensorsPermission(activity: Activity) {
        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.BODY_SENSORS), REQUEST_BODY_SENSORS)
    }

    fun checkAndRequestPermissions(activity: Activity) {
        val allPermissions = mutableMapOf<String, Int>(
            Manifest.permission.ACTIVITY_RECOGNITION to REQUEST_ACTIVITY_RECOGNITION,
            Manifest.permission.BODY_SENSORS to REQUEST_BODY_SENSORS
        )

        // For Android Tiramisu (13) and above, add POST_NOTIFICATIONS permission if needed
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            allPermissions[Manifest.permission.POST_NOTIFICATIONS] = REQUEST_PERMISSIONS_CODE
        }

        val permissionsToRequest = allPermissions.filterKeys { permission ->
            ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED
        }.keys.toTypedArray()

        // Request all not granted permissions
        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(activity, permissionsToRequest, REQUEST_PERMISSIONS_CODE)
        } else {
            // All permissions are granted; you might want to proceed with operations that require permissions here
        }
    }
    fun arePermissionsGranted(activity: Activity): Boolean {
        return checkActivityRecognitionPermission(activity) && checkBodySensorsPermission(activity) &&
                (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU || checkNotificationPermission(activity))
    }

    private fun checkNotificationPermission(activity: Activity): Boolean {
        return ContextCompat.checkSelfPermission(activity, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
    }
}
