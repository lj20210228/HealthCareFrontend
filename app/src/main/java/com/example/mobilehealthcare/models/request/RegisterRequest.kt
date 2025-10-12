package com.example.mobilehealthcare.models.request

import android.os.Parcelable
import com.example.mobilehealthcare.domain.Doctor
import com.example.mobilehealthcare.domain.Patient
import com.example.mobilehealthcare.domain.User
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable


@Parcelize
@Serializable
data class RegisterRequest(
    val user: User,
    val doctor: Doctor?=null,
    val patient: Patient?=null,
): Parcelable
