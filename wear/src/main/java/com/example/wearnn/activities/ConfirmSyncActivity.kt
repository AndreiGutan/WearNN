package com.example.wearnn.activities

import android.app.Activity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.wearnn.R

class ConfirmSyncActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Assuming the email to sync is passed as an intent extra
        val emailToSync = intent.getStringExtra("email") ?: return // Exit if null

        // Create and show the AlertDialog
        AlertDialog.Builder(this).apply {
            // Inflate and set the layout for the dialog
            // Pass null as the parent view because it's going in the dialog layout
            val dialogView = layoutInflater.inflate(R.layout.dialog_confirm_sync, null)
            setView(dialogView)

            // Access the TextView from the dialog layout to set the email
            val tvConfirmSync = dialogView.findViewById<TextView>(R.id.tvConfirmSync)
            tvConfirmSync.text = getString(R.string.confirm_sync_with_email, emailToSync)

            // Set up the buttons
            setPositiveButton("Yes") { dialog, which ->
                // Handle the confirmation action here
            }
            setNegativeButton("No") { dialog, which ->
                // Handle the cancellation here
            }
        }.show()
    }
}
