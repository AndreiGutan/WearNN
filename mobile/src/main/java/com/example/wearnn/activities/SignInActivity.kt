package com.example.wearnn.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.wearnn.R
import com.example.wearnn.utils.WearOSCommunicationManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class SignInActivity : AppCompatActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        // Configure Google Sign-In options.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        // Build a GoogleSignInClient with the specified options.
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Check for an already existing sign-in account.
        // This check is important to avoid asking the user to sign in each time the app starts.
        val alreadySignedInAccount = GoogleSignIn.getLastSignedInAccount(this)
        if (alreadySignedInAccount != null) {
            // User is already signed in. Handle the signed-in user's details.
            updateUI(alreadySignedInAccount)
        } else {
            // Initialize the sign-in button and its click listener.
            findViewById<Button>(R.id.sign_in_button).setOnClickListener {
                signIn()
            }
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

// Additional methods such as onActivityResult, handleSignInResult, and updateUI would remain the same.


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            // Signed in successfully, show authenticated UI.
            updateUI(account)
            WearOSCommunicationManager(this).sendAuthTokenToWear(account.idToken ?: "")
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            updateUI(null)
        }
    }

    private fun updateUI(account: GoogleSignInAccount?) {
        if (account != null) {
            // Navigate to your main activity or update the UI to show signed-in state
        } else {
            // Show error or prompt to sign in again
        }
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }
}
