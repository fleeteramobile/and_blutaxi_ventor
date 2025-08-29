package com.blueventor.session

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(@ApplicationContext context: Context) {
    private val PREF_NAME = "BlueVentorPref"
    private val sharedPref: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun saveString(key: String, value: String) {
        sharedPref.edit().putString(key, value).apply()
    }

    fun getString(key: String, defaultValue: String = ""): String {
        return sharedPref.getString(key, defaultValue) ?: defaultValue
    }

    fun clearSession() {
        sharedPref.edit().clear().apply()
    }

    // Add other methods as needed (e.g., saveInt, saveBoolean)
    fun saveInt(key: String, value: Int) {
        sharedPref.edit().putInt(key, value).apply()
    }

    fun getInt(key: String, defaultValue: Int = -1): Int {
        return sharedPref.getInt(key, defaultValue)
    }

    fun saveBoolean(key: String, value: Boolean) {
        sharedPref.edit().putBoolean(key, value).apply()
    }

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return sharedPref.getBoolean(key, defaultValue)
    }

}