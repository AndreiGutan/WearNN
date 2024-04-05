package com.example.wearnn.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType

object PermissionUtils {
    const val REQUEST_ACTIVITY_RECOGNITION = 1
    const val REQUEST_COARSE_LOCATION = 2
    const val REQUEST_FINE_LOCATION = 3
    const val REQUEST_BODY_SENSORS = 4
    const val REQUEST_PERMISSIONS_CODE = 100
    fun requestGoogleFitPermissions(activity: Activity) {
        val fitnessOptions = FitnessOptions.builder()
            .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
            .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_WRITE)
            // Add other data types here as per your requirement
            .build()

    }
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

    fun checkAndRequestPermissions(activity: Activity) {
        // Aggregate all permissions to request
        val allPermissions = mutableMapOf(
            Manifest.permission.ACTIVITY_RECOGNITION to REQUEST_ACTIVITY_RECOGNITION,
            Manifest.permission.ACCESS_COARSE_LOCATION to REQUEST_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION to REQUEST_FINE_LOCATION,
            Manifest.permission.BODY_SENSORS to REQUEST_BODY_SENSORS
        )

        // For Android Tiramisu (13) and above, add POST_NOTIFICATIONS permission
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
            // For example, initializing components that require these permissions
        }

    }
}
