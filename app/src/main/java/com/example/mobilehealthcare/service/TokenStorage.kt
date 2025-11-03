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
    fun saveUserId(id: String) {
        prefs.edit { putString("userId", id) }
    }
    fun saveDoctorId(doctorId: String) {
        prefs.edit { putString("doctorId", doctorId) }
    }
    fun savePatientId(patientId: String) {
        prefs.edit { putString("patientId", patientId) }
    }
    fun saveHospitalId(hospitalId: String) {
        prefs.edit { putString("hospitalId", hospitalId) }
    }

    fun getToken(): String? = prefs.getString("jwt_token", null)
    fun getRole(): String? = prefs.getString("role", null)
    fun getUserId(): String?=prefs.getString("userId",null)
    fun getDoctorId(): String?=prefs.getString("doctorId",null)

    fun getHospitalId(): String?=prefs.getString("hospitalId",null)
    fun getPatientId(): String?=prefs.getString("patientId",null)




    fun clearToken() {
        prefs.edit { remove("jwt_token") }
    }
    fun clearRole() {
        prefs.edit { remove("role") }
    }
    fun clearUserId(){
        prefs.edit { remove("userId") }
    }
    fun clearDoctorId(){
        prefs.edit { remove("doctorId") }
    }
    fun clearHospitalId(){
        prefs.edit { remove("hospitalId") }
    }
    fun clearPatientId(){
        prefs.edit {remove("patientId") }
    }
}