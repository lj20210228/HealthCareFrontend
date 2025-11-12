package com.example.mobilehealthcare.domain

import kotlinx.serialization.Serializable
import java.sql.Struct
@Serializable
data class SelectedDoctor(
    val doctorId: String,
    val patientId: String
)
