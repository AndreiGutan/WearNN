package com.example.wearnn.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType

object PermissionUtils {
    const val REQUEST_ACTIVITY_RECOGNITION = 1
    const val REQUEST_COARSE_LOCATION = 2
    const val REQUEST_FINE_LOCATION = 3
    const val REQUEST_BODY_SENSORS = 4
    const val GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = 5 // Added for Google Fit
    const val REQUEST_PERMISSIONS_CODE = 100
    fun requestGoogleFitPermissions(activity: Activity) {
        val fitnessOptions = FitnessOptions.builder()
            .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
            .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_WRITE)
            // Add other data types here as per your requirement
            .build()

        if (!GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(activity), fitnessOptions)) {
            GoogleSignIn.requestPermissions(
                activity, // your activity
                GOOGLE_FIT_PERMISSIONS_REQUEST_CODE, // e.g., 5
                GoogleSignIn.getLastSignedInAccount(activity),
                fitnessOptions)
        } else {
            // Here, directly accessing Google Fit data after checking permissions
            // might not be appropriate since the user hasn't interacted with your app yet.
            // Consider calling a method that sets up your UI or data fetching here.
        }
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
        val allPermissions = mapOf(
            Manifest.permission.ACTIVITY_RECOGNITION to REQUEST_ACTIVITY_RECOGNITION,
            Manifest.permission.ACCESS_COARSE_LOCATION to REQUEST_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION to REQUEST_FINE_LOCATION,
            Manifest.permission.BODY_SENSORS to REQUEST_BODY_SENSORS
        )

        val permissionsToRequest = allPermissions.filterKeys { permission ->
            ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED
        }.keys.toTypedArray()

        // Request all not granted permissions
        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(activity, permissionsToRequest, REQUEST_PERMISSIONS_CODE )
        } else {
            // All permissions are granted; you might want to proceed with operations that require permissions here
            // For example, initializing components that require these permissions
        }

        // Separately handle the request for Google Fit permissions, which follows a different flow
        requestGoogleFitPermissions(activity)
    }
}
