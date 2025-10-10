package com.example.mobilehealthcare.domain

import java.sql.Struct

data class SelectedDoctor(
    val doctorId: String,
    val patientId: String
)
