package com.example.wearnn.utils

import android.content.Context

object PreferencesHelper {
    const val PREFS_NAME = "WearAppPrefs"
    private const val IS_LOGGED_IN = "isLoggedIn"
    const val USER_EMAIL = "userEmail"
    private const val USER_PASSWORD = "userPassword"

    private const val KEY_ACCOUNT_SYNCED = "account_synced"

    fun isLoggedIn(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false)
    }

    fun setLoggedIn(context: Context, loggedIn: Boolean) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean(IS_LOGGED_IN, loggedIn).apply()
    }

    fun getUserEmail(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(USER_EMAIL, null) // Ensure "userEmail" is the key used during storing
    }

    fun setUserEmail(context: Context, email: String) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(USER_EMAIL, email).apply() // Again, ensure consistency in key
    }

    fun getUserPassword(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(USER_PASSWORD, null)
    }

    fun setUserPassword(context: Context, password: String) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(USER_PASSWORD, password).apply()
    }

    fun isAccountSyncedWithWear(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(KEY_ACCOUNT_SYNCED, false)
    }

    fun setAccountSyncedWithWear(context: Context, synced: Boolean) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean(KEY_ACCOUNT_SYNCED, synced).apply()
    }
}
