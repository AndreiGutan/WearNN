package com.example.wearnn.services

import android.content.Intent
import android.util.Log
import com.example.wearnn.activities.ConfirmSyncActivity
import com.example.wearnn.utils.PreferencesHelper
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService

class WearDataLayerListenerService : WearableListenerService() {
    override fun onDataChanged(dataEvents: DataEventBuffer) {
        dataEvents.forEach { event ->
            if (event.type == DataEvent.TYPE_CHANGED && event.dataItem.uri.path == "/yourPath") {
                DataMapItem.fromDataItem(event.dataItem).dataMap.apply {
                    // Handle your data here
                }
            }
        }
    }
    override fun onMessageReceived(messageEvent: MessageEvent) {
        super.onMessageReceived(messageEvent)
        if (messageEvent.path == ACCOUNT_SYNC_PATH) {
            val receivedEmail = String(messageEvent.data, Charsets.UTF_8)
            Log.d("WearService", "Received user email: $receivedEmail")

            val storedEmail = PreferencesHelper.getUserEmail(applicationContext)

            if (storedEmail.isNullOrBlank() || storedEmail != receivedEmail) {
                // No email is stored, or the stored email does not match the received email
                showConfirmSyncActivity(receivedEmail)
                Log.d("WearService", "No email is stored, or it doesn't match: $storedEmail")
            } else {
                // Email matches, account is already synced
                Log.d("WearService", "Account is already synced with $storedEmail")
            }
        }
    }

    private fun showConfirmSyncActivity(userEmail: String) {
        val intent = Intent(this, ConfirmSyncActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            putExtra("email", userEmail)
        }
        startActivity(intent)
    }

    companion object {
        private const val ACCOUNT_SYNC_PATH = "/sync_account"
    }
}
