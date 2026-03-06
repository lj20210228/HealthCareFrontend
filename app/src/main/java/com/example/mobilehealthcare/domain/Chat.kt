package com.example.mobilehealthcare.domain

import kotlinx.serialization.Serializable

@Serializable
data class Chat(
    val id: String,
    val doctorId: String,
    val patientId: String
)
