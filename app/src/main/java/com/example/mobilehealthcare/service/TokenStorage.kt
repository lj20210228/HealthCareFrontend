package com.example.mobilehealthcare.service

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.mobilehealthcare.domain.Role
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TokenStorage @Inject constructor(@ApplicationContext context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        prefs.edit { putString("jwt_token", token) }
    }
    fun saveRole(role: String) {
        prefs.edit { putString("role", role) }
    }

    fun getToken(): String? = prefs.getString("jwt_token", null)
    fun getRole(): String? = prefs.getString("role", null)


    fun clearToken() {
        prefs.edit { remove("jwt_token") }
    }
    fun clearRole() {
        prefs.edit { remove("role") }
    }
}