package com.example.wearnn.activities

import MetricsScreen
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.ViewModelProvider
import com.example.wearnn.R
import com.example.wearnn.data.database.AppDatabase
import com.example.wearnn.presentation.theme.MobileNNTheme
import com.example.wearnn.utils.HealthViewModelFactory
import com.example.wearnn.utils.PreferencesHelper
import com.example.wearnn.viewModel.HealthViewModel
import com.google.android.gms.wearable.Wearable


open class DashboardActivity : AppCompatActivity() {
    private lateinit var healthViewModel: HealthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Set up the Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        setupViewModel()
        initializeUI()

        // Initialize your UI here
        //adds user email to top bar
        val userEmailTextView: TextView = findViewById(R.id.userEmailTextView)
        userEmailTextView.text = PreferencesHelper.getUserEmail(applicationContext)

        // Setup the Compose content
        findViewById<ComposeView>(R.id.MetricsScreen).setContent {
            MobileNNTheme { // Assuming you have a theme like in your wear app
                MetricsScreen(healthViewModel) // You would need to define this Composable function
            }
        }

    }

    private fun setupViewModel() {
        val database = AppDatabase.getDatabase(this)
        val healthDataDao = database.healthDataDao()
        val viewModelFactory = HealthViewModelFactory(healthDataDao)
        healthViewModel = ViewModelProvider(this, viewModelFactory)[HealthViewModel::class.java]
    }

    private fun initializeUI() {
        val userEmailTextView: TextView = findViewById(R.id.userEmailTextView)
        userEmailTextView.text = PreferencesHelper.getUserEmail(applicationContext)
    }

    private fun checkForConnectedWearDevices() {
        val nodeClient = Wearable.getNodeClient(this)
        nodeClient.connectedNodes.addOnSuccessListener { nodes ->
            if (nodes.isNotEmpty()) {
                nodes.forEach { node ->
                    // Here, you could check the account sync status for each node
                    syncAccountToWear()
                    Log.d("DashboardActivity", "Connected node: ${node.displayName}")
                }
            } else {
                Log.d("DashboardActivity", "No Wear devices connected.")
            }
        }
    }

    private fun syncAccountToWear() {
        val userEmail = PreferencesHelper.getUserEmail(applicationContext)?.toByteArray()
        Wearable.getNodeClient(applicationContext).connectedNodes.addOnSuccessListener { nodes ->
            nodes.forEach { node ->
                Wearable.getMessageClient(this)
                    .sendMessage(node.id, Companion.ACCOUNT_SYNC_PATH, userEmail).apply {
                    addOnSuccessListener {
                        Log.d(
                            "DashboardActivity",
                            "Account sync request sent to ${node.displayName}"
                        )
                    }
                    addOnFailureListener {
                        Log.e("DashboardActivity", "Failed to send account sync request", it)
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.syncWearButton -> {
                checkForConnectedWearDevices()
                Toast.makeText(this, "Sync Selected", Toast.LENGTH_SHORT).show()
            }

            R.id.menuNotification -> Toast.makeText(
                this,
                "Notifications Selected",
                Toast.LENGTH_SHORT
            ).show()

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
            R.id.logoutButton -> {
                PreferencesHelper.setLoggedIn(applicationContext, false)
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val ACCOUNT_SYNC_PATH = "/sync_account"
    }
}
