package com.example.wearnn.activities

import android.util.Log;
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
            .requestIdToken(getString(R.string.server_client_id))
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
        Log.d("SignInActivity", "onActivityResult with requestCode: $requestCode, resultCode: $resultCode")
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            // Signed in successfully, show authenticated UI.
            Log.d("SignInActivity", "Sign-in success. Account: ${account.email}")
            updateUI(account)
            WearOSCommunicationManager(this).sendAuthTokenToWear(account.idToken ?: "")
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            Log.d("SignInActivity", "signInResult:failed code=" + e.statusCode)
            updateUI(null)
        }
    }

    private fun updateUI(account: GoogleSignInAccount?) {
        if (account != null) {
            // User is signed in
            val dashboardIntent = Intent(this, DashboardActivity::class.java).apply {
                putExtra("userEmail", account.email)
            }
            startActivity(dashboardIntent)
            finish()
        } else {
            // Show error or prompt to sign in again
            // For example, you can make the sign-in button visible again and show a Toast message
        }
    }


    companion object {
        private const val RC_SIGN_IN = 9001
    }
}
