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

    fun logout() {
        pref.edit { clear() }
    }

    companion object {
        private const val PREF_NAME = "QcuciSessionPref"
        private const val KEY_IS_LOGIN = "isLogin"
        private const val KEY_USERNAME = "username"
    }
}