package com.example.wearnn.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.wearnn.R
import com.example.wearnn.utils.PreferencesHelper
import com.google.android.gms.wearable.Wearable

class DashboardActivity : AppCompatActivity() {

//    top menu bar
    override  fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.syncWearButton -> {
                syncAccountToWear()
                Toast.makeText(this, "Sync Selected", Toast.LENGTH_SHORT).show()
            }
            R.id.menuNotification -> Toast.makeText(this, "Notifications Selected", Toast.LENGTH_SHORT).show()
            R.id.settings -> Toast.makeText(this, "Settings Selected", Toast.LENGTH_SHORT).show()
            R.id.share -> Toast.makeText(this, "Share Selected", Toast.LENGTH_SHORT).show()
            R.id.aboutus -> Toast.makeText(this, "About Us Selected", Toast.LENGTH_SHORT).show()
            R.id.logoutButton ->  { PreferencesHelper.setLoggedIn(applicationContext, false)
                                    startActivity(Intent(this, LoginActivity::class.java))
                                    finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
//  end of  top menu bar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_dashboard)
        val userEmailTextView: TextView = findViewById(R.id.userEmailTextView)
        userEmailTextView.text = PreferencesHelper.getUserEmail(applicationContext)
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

