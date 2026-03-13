package com.example.mobilehealthcare.domain

import com.example.mobilehealthcare.date.LocalDateTimeSerializer
import com.example.mobilehealthcare.date.StrictLocalDateSerializer
import com.example.mobilehealthcare.date.StrictLocalTimeSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
@Serializable
data class Message(
    val id: String?=null,
    val senderId: String,
    val recipientId: String,
    val content: String,
    @Serializable(with = LocalDateTimeSerializer::class)
    val timeStamp: LocalDateTime?= LocalDateTime.now(),
    val chatId: String
)
