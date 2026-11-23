package com.example.mlk_pocari

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class PrefManager(context: Context) {

    private val pref: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    var isLoggedIn: Boolean
        get() = pref.getBoolean(KEY_IS_LOGIN, false)
        set(value) {
            pref.edit { putBoolean(KEY_IS_LOGIN, value) }
        }

    var username: String
        get() = pref.getString(KEY_USERNAME, "") ?: ""
        set(value) {
            pref.edit { putString(KEY_USERNAME, value) }
        }

    var userId: Long
        get() = pref.getLong(KEY_USER_ID, -1L)
        set(value) {
            pref.edit { putLong(KEY_USER_ID, value) }
        }

    var isOnboardingCompleted: Boolean
        get() = pref.getBoolean(KEY_ONBOARDING_COMPLETED, false)
        set(value) {
            pref.edit { putBoolean(KEY_ONBOARDING_COMPLETED, value) }
        }

    fun logout() {
        val onboardingDone = isOnboardingCompleted
        pref.edit { clear() }
        isOnboardingCompleted = onboardingDone
    }

    companion object {
        private const val PREF_NAME = "QcuciSessionPref"
        private const val KEY_IS_LOGIN = "isLogin"
        private const val KEY_USERNAME = "username"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"
    }
}