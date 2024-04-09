package com.example.wearnn.services

import android.content.Intent
import android.util.Log
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService
import com.example.wearnn.utils.PreferencesHelper

class MessageLayerListenerService : WearableListenerService() {

    override fun onMessageReceived(messageEvent: MessageEvent) {
        super.onMessageReceived(messageEvent)

        // Checking the path of the message to distinguish between different types of messages
        when (messageEvent.path) {
            "/account_sync" -> {
                val message = String(messageEvent.data)
                Log.d("MessageLayerService", "Received sync message: $message")

                // Check if the message indicates a confirmed sync
                if (message == "confirmed") {
                    // Handle confirmed sync, e.g., by updating shared preferences
                    PreferencesHelper.setAccountSyncedWithWear(applicationContext, true)

                    // Optionally, notify the user or the app via a broadcast or direct action
                    val intent = Intent("com.example.ACTION_SYNC_CONFIRMED")
                    sendBroadcast(intent)
                }
                else
                {
                    PreferencesHelper.setAccountSyncedWithWear(applicationContext, false)
                }
            }
        }
    }
}
