package com.example.mobilehealthcare.domain

import com.example.mobilehealthcare.date.StrictLocalDateSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDate
@Serializable
data class Recipe(
    val id:String?=null,
    val patientId: String,
    val doctorId: String,
    val medication:String,
    val quantity: Int,
    val instructions: String,
    @Serializable(with = StrictLocalDateSerializer::class)
    val dateExpired: LocalDate
)
