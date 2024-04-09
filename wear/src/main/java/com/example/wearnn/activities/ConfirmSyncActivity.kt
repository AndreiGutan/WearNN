package com.example.wearnn.activities

import android.os.Bundle
import android.app.Activity
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.wearnn.R
import com.example.wearnn.utils.PreferencesHelper
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.tasks.await

class ConfirmSyncActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val emailToSync = intent.getStringExtra("email") ?: return // Exit if null

        val dialogView = layoutInflater.inflate(R.layout.dialog_confirm_sync, null)
        val tvConfirmSync = dialogView.findViewById<TextView>(R.id.tvConfirmSync)
        tvConfirmSync.text = getString(R.string.confirm_sync_with_email, emailToSync)

        AlertDialog.Builder(this).apply {
            setView(dialogView)
            setPositiveButton("Yes") { _, _ ->
                CoroutineScope(Dispatchers.IO).launch {
                    sendMessageToMobile("/account_sync", "confirmed".toByteArray())
                    saveEmailToStorage(emailToSync) // Save the email once confirmed using PreferencesHelper
                    withContext(Dispatchers.Main) {
                        Toast.makeText(applicationContext, "Syncing account...", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            setNegativeButton("No") { _, _ ->
                Toast.makeText(applicationContext, "Account sync cancelled.", Toast.LENGTH_SHORT).show()
            }
        }.show()
    }

    private suspend fun sendMessageToMobile(path: String, message: ByteArray) {
        val nodes = Wearable.getNodeClient(applicationContext).connectedNodes.await()
        for (node in nodes) {
            Wearable.getMessageClient(applicationContext).sendMessage(node.id, path, message).await()
        }
    }

    private fun saveEmailToStorage(email: String) {
        PreferencesHelper.setUserEmail(applicationContext, email)
    }
}
