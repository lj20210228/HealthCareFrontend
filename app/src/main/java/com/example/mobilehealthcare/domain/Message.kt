package com.example.mobilehealthcare.domain

import com.example.mobilehealthcare.date.StrictLocalDateSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
@Serializable
data class Message(
    val id: String,
    val senderId: String,
    val recipientId: String,
    val content: String,
    @Contextual
    val timeStamp: LocalDateTime?= LocalDateTime.now(),
    val chatId: String
)
