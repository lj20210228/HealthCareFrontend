package com.example.mobilehealthcare.domain

import kotlinx.serialization.Serializable

@Serializable
data class Chat(
    val id: String?=null,
    val doctorId: String,
    val patientId: String
)
