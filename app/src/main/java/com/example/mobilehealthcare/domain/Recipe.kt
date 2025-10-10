package com.example.mobilehealthcare.domain

import java.time.LocalDate

data class Recipe(
    val id:String,
    val patientId: String,
    val doctorId: String,
    val medication:String,
    val quantity: String,
    val instructions: String,
    val dateExpired: LocalDate
)
