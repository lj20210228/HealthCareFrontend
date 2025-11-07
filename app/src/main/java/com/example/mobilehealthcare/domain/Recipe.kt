package com.example.mobilehealthcare.domain

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDate
@Serializable
data class Recipe(
    val id:String,
    val patientId: String,
    val doctorId: String,
    val medication:String,
    val quantity: String,
    val instructions: String,
    @Contextual
    val dateExpired: LocalDate
)
