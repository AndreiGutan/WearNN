package com.example.wearnn.services
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.wearnn.activities.ConfirmSyncActivity

import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService

class WearDataLayerListenerService : WearableListenerService() {

    override fun onMessageReceived(messageEvent: MessageEvent) {
        super.onMessageReceived(messageEvent)
        if (messageEvent.path == ACCOUNT_SYNC_PATH) {
            val receivedEmail = String(messageEvent.data, Charsets.UTF_8)
            Log.d("WearService", "Received user email: $receivedEmail")

            val storedEmail = getStoredEmail() // Implement this method

            if (storedEmail.isNullOrBlank() || storedEmail != receivedEmail) {
                // No email is stored, or the stored email does not match the received email
                showConfirmSyncActivity(receivedEmail)
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

    private fun getStoredEmail(): String? {
        // Implement fetching the stored email, possibly using SharedPreferences
        // For example:
        val prefs = applicationContext.getSharedPreferences("YourPrefName", Context.MODE_PRIVATE)
        return prefs.getString("userEmailKey", null)
    }

    companion object {
        private const val ACCOUNT_SYNC_PATH = "/sync_account"
    }
}

