package com.bangkit.japorapp.utils

import android.content.Context
import android.content.SharedPreferences
import com.bangkit.japorapp.model.User

class UserPreference(context: Context) {

    companion object {
        private const val PREFS_NAME = "user_pref"
        private const val NAME = "uid"
        private const val NIK = "username"
        private const val IMG_URL = "img_url"
        private const val EMAIL = "email"
    }

    private val preferences: SharedPreferences by lazy {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun setUser(value: User) {
        val editor = preferences.edit()

        editor.putString(NAME, value.fullName)
        editor.putString(NIK, value.nik)
        editor.putString(IMG_URL, value.imgUrl)
        editor.putString(EMAIL, value.email)

        editor.apply()
    }

    fun getUser() : User {
        val user = User()

        user.fullName = preferences.getString(NAME, "") ?: ""
        user.nik = preferences.getString(NIK, "") ?: ""
        user.imgUrl = preferences.getString(IMG_URL, "") ?: ""
        user.email = preferences.getString(EMAIL, "") ?: ""

        return user
    }

    fun clearPrefs() {
        preferences.edit().clear().apply()
    }

}