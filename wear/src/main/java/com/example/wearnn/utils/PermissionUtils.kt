package com.example.yourapp.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionUtils {
    const val REQUEST_ACTIVITY_RECOGNITION = 1
    const val REQUEST_COARSE_LOCATION = 2
    const val REQUEST_FINE_LOCATION = 3
    const val REQUEST_BODY_SENSORS = 4

    fun checkActivityRecognitionPermission(activity: Activity): Boolean =
        ContextCompat.checkSelfPermission(activity, Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_GRANTED

    fun requestActivityRecognitionPermission(activity: Activity) {
        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACTIVITY_RECOGNITION), REQUEST_ACTIVITY_RECOGNITION)
    }

    fun checkCoarseLocationPermission(activity: Activity): Boolean =
        ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

    fun requestCoarseLocationPermission(activity: Activity) {
        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_COARSE_LOCATION)
    }

    fun checkFineLocationPermission(activity: Activity): Boolean =
        ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

    fun requestFineLocationPermission(activity: Activity) {
        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_FINE_LOCATION)
    }

    fun checkBodySensorsPermission(activity: Activity): Boolean =
        ContextCompat.checkSelfPermission(activity, Manifest.permission.BODY_SENSORS) == PackageManager.PERMISSION_GRANTED

    fun requestBodySensorsPermission(activity: Activity) {
        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.BODY_SENSORS), REQUEST_BODY_SENSORS)
    }
}
