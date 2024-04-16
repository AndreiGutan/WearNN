package com.example.wearnn.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.wearnn.R
import com.example.wearnn.utils.PreferencesHelper
import com.google.android.gms.wearable.Node
import com.google.android.gms.wearable.Wearable

open class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Initialize your UI here
        //adds user email to top bar
        val userEmailTextView: TextView = findViewById(R.id.userEmailTextView)
        userEmailTextView.text = PreferencesHelper.getUserEmail(applicationContext)


    }

    private fun checkForConnectedWearDevices() {
        val nodeClient = Wearable.getNodeClient(this)
        nodeClient.connectedNodes.addOnSuccessListener { nodes ->
            if (nodes.isNotEmpty()) {
                nodes.forEach { node ->
                    // Here, you could check the account sync status for each node
                    // For simplicity, let's log the node's display name
                    syncAccountToWear()
                    Log.d("DashboardActivity", "Connected node: ${node.displayName}")
                }
            } else {
                Log.d("DashboardActivity", "No Wear devices connected.")
            }
        }
    }


    private fun checkAccountSyncStatus(nodes: List<Node>) {
        val isAccountSynced = PreferencesHelper.isAccountSyncedWithWear(this)

        if (!isAccountSynced) {
            Log.d("DashboardActivity", "Account are not in sync")
            promptAccountSync()
        } else {
            Log.d("DashboardActivity", "Account are already in sync")
            syncAccountToWear()
        }
    }
    private fun promptAccountSync() {
        // Show a dialog or a custom view that slides down from the top.
        // Here's a simple dialog example:

        AlertDialog.Builder(this)
            .setTitle("Account Sync Needed")
            .setMessage("Your account is not synced with your Wear device. Please log in on your Wear device to sync.")
            .setPositiveButton("OK") { dialog, which ->
                // Optionally, provide a button to open a help page or send a message to Wear to start the login activity.
            }
            .setNegativeButton("Cancel", null)
            .show()
    }


    //    top menu bar
    override  fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.syncWearButton -> {
                checkForConnectedWearDevices()
                Toast.makeText(this, "Sync Selected", Toast.LENGTH_SHORT).show()
            }
            R.id.menuNotification -> Toast.makeText(this, "Notifications Selected", Toast.LENGTH_SHORT).show()
            R.id.settings -> Toast.makeText(this, "Settings Selected", Toast.LENGTH_SHORT).show()
            R.id.share -> {
                val sendIntent: Intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, "This is my text to send.")
                            type = "text/plain"
                        }
                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            }
            R.id.aboutus -> startActivity(Intent(this, AboutUsActivity::class.java))
            R.id.logoutButton ->  { PreferencesHelper.setLoggedIn(applicationContext, false)
                                    startActivity(Intent(this, LoginActivity::class.java))
                                    finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val ACCOUNT_SYNC_PATH = "/sync_account"
    }

    private fun syncAccountToWear() {
        val userEmail = PreferencesHelper.getUserEmail(applicationContext)?.toByteArray()
        Wearable.getNodeClient(applicationContext).connectedNodes.addOnSuccessListener { nodes ->
            nodes.forEach { node ->
                Wearable.getMessageClient(this).sendMessage(node.id, ACCOUNT_SYNC_PATH, userEmail).apply {
                    addOnSuccessListener {
                        Log.d("DashboardActivity", "Account sync request sent to ${node.displayName}")
                    }
                    addOnFailureListener {
                        Log.e("DashboardActivity", "Failed to send account sync request", it)
                    }
                }
            }
        }
    }


}

