package com.example.wearnn.utils

import android.content.Context

object PreferencesHelper {
    private const val PREFS_NAME = "WearAppPrefs"
    private const val IS_LOGGED_IN = "isLoggedIn"
    private const val USER_EMAIL = "userEmail"
    private const val KEY_ACCOUNT_SYNCED = "account_synced"

    fun isLoggedIn(context: Context): Boolean =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getBoolean(IS_LOGGED_IN, false)

    fun setLoggedIn(context: Context, loggedIn: Boolean) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit()
            .putBoolean(IS_LOGGED_IN, loggedIn).apply()
    }

    fun getUserEmail(context: Context): String? =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(USER_EMAIL, null)

    fun setUserEmail(context: Context, email: String) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit()
            .putString(USER_EMAIL, email).apply()
    }

    fun isAccountSyncedWithWear(context: Context): Boolean =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getBoolean(KEY_ACCOUNT_SYNCED, false)

    fun setAccountSyncedWithWear(context: Context, synced: Boolean) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit()
            .putBoolean(KEY_ACCOUNT_SYNCED, synced).apply()
    }
}
