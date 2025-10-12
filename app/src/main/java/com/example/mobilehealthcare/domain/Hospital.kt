package com.example.mobilehealthcare.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Hospital(
    val id: String,
    val name: String,
    val city: String,
    val address: String
): Parcelable
