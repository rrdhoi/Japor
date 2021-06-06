package com.bangkit.japorapp.utils

import android.content.Context
import android.content.SharedPreferences
import com.bangkit.japorapp.data.response.UserResponse

class UserPreference(context: Context) {

    companion object {
        private const val PREFS_NAME = "user_pref"
        private const val ID = "id"
        private const val NAMA = "nama"
        private const val NIK = "nik"
        private const val PASSWORD = "password"
        private const val URL = "url"
        private const val DEPARTEMEN = "departemen"
    }

    private val preferences: SharedPreferences by lazy {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun setUser(value: UserResponse) {
        val editor = preferences.edit()

        editor.putString(ID, value.id)
        editor.putString(NAMA, value.nama)
        editor.putString(NIK, value.nik)
        editor.putString(PASSWORD, value.password)
        editor.putString(URL, value.url)
        editor.putString(DEPARTEMEN, value.departemen)

        editor.apply()
    }

    fun getUser() : UserResponse {
        val user = UserResponse()

        user.id = preferences.getString(ID, "") ?: ""
        user.nama = preferences.getString(NAMA, "") ?: ""
        user.nik = preferences.getString(NIK, "") ?: ""
        user.password = preferences.getString(PASSWORD, "") ?: ""
        user.url = preferences.getString(URL, "") ?: ""
        user.departemen = preferences.getString(DEPARTEMEN, "") ?: ""

        return user
    }

    fun clearPrefs() {
        preferences.edit().clear().apply()
    }

}