package com.example.mobilehealthcare.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
@Serializable
@Parcelize
data class Doctor(
    val id: String?=null,
    val  userId: String?=null,
    val fullName:String,
    val specialization: String,
    val maxPatients:Int,
    val currentPatients:Int=0,
    val hospitalId: String?=null,
    val isGeneral: Boolean=false
): Parcelable