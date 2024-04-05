package com.example.wearnn.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.wearnn.R
import com.example.wearnn.utils.PreferencesHelper
import com.google.android.gms.tasks.Task
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.PutDataRequest
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val userEmailTextView: TextView = findViewById(R.id.userEmailTextView)
        val logoutButton: Button = findViewById(R.id.logoutButton)
       // val syncWearButton: Button = findViewById(R.id.syncWearButton) // The new button

        userEmailTextView.text = PreferencesHelper.getUserEmail(applicationContext)

        logoutButton.setOnClickListener {
            PreferencesHelper.setLoggedIn(applicationContext, false)
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        val syncButton: Button = findViewById(R.id.syncWearButton) // Assuming you've added this button
        syncButton.setOnClickListener {
            syncAccountToWear()
        }


    }
    companion object {
        private const val ACCOUNT_SYNC_PATH = "/sync_account"
    }

    private fun syncAccountToWear() {
        // Get the connected nodes (Wear devices)
        val nodesTask = Wearable.getNodeClient(applicationContext).connectedNodes
        nodesTask.addOnSuccessListener { nodes ->
            val nodeId = nodes.firstOrNull()?.id // Assuming you're sending to the first connected device

            nodeId?.let {
                // Prepare and send the message
                Wearable.getMessageClient(this).sendMessage(nodeId, ACCOUNT_SYNC_PATH, null).apply {
                    addOnSuccessListener {
                        Log.d("DashboardActivity", "Message sent successfully")
                    }
                    addOnFailureListener {
                        Log.e("DashboardActivity", "Message failed", it)
                    }
                }
            }
        }
    }

}

