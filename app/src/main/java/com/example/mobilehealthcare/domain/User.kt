package com.example.mobilehealthcare.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class User(
    val email:String,
    val password:String,
    val role:Role,
    val id: String?=null
): Parcelable
