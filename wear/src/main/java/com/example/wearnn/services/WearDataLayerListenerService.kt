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
        createNotificationChannel()
    }

    override fun onMessageReceived(messageEvent: MessageEvent) {

        super.onMessageReceived(messageEvent)
        if (messageEvent.path == "/account_info") {
            val accountInfo = String(messageEvent.data)
            Log.d("WearService", "Received account info: $accountInfo")
            // Process the account info here
        }
    }

    private fun showConnectAccountNotification(accountEmail: String) {
        // Check for POST_NOTIFICATIONS permission before attempting to show a notification
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            val notificationId = 1
            val confirmIntent = Intent(this, YourConfirmationActivity::class.java).apply {
                putExtra("accountEmail", accountEmail)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val confirmPendingIntent: PendingIntent = PendingIntent.getActivity(
                this, 0, confirmIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification) // Ensure you have this drawable resource
                .setContentTitle("Connect Account")
                .setContentText("Do you want to connect with account: $accountEmail?")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(confirmPendingIntent)
                .addAction(R.drawable.ic_confirm, "Yes", confirmPendingIntent) // Ensure you have this drawable resource
                .setAutoCancel(true)
                .build()

            NotificationManagerCompat.from(this).notify(notificationId, notification)
        } else {
            // Log or handle the case where the permission isn't granted.
            // Note: Directly requesting this permission from a service isn't typical. You might need to inform the user through other means.
            Log.d("WearDataLayerListener", "POST_NOTIFICATIONS permission not granted. Cannot show notification.")
        }
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    companion object {
        private const val CHANNEL_ID = "account_sync_channel"
    }
}
