package com.example.mobilehealthcare.domain
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
@Parcelize
@Serializable
data class Patient(
    val id: String?=null,
    val userId:String?=null,
    val fullName: String,
    val hospitalId: String,
    val jmbg: String
): Parcelable