package com.example.wearnn.services
import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.wearnn.R
import com.example.wearnn.activities.YourConfirmationActivity
import com.example.wearnn.utils.PermissionUtils
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService

class WearDataLayerListenerService : WearableListenerService() {

    override fun onCreate() {
        super.onCreate()
    }

    override fun onMessageReceived(messageEvent: MessageEvent) {
        super.onMessageReceived(messageEvent)
        if (messageEvent.path == ACCOUNT_SYNC_PATH) {
            // Logic to handle the account sync request
            // For instance, start an activity or sync operation
            Log.d("WearService", "Sync request received.")
        }
    }

    companion object {
        private const val ACCOUNT_SYNC_PATH = "/sync_account"
    }

}
