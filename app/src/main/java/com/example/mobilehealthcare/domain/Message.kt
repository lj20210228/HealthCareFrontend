package com.example.mobilehealthcare.domain

import java.time.LocalDateTime

data class Message(
    val id: String,
    val senderId: String,
    val recipientId: String,
    val content: String,
    val timeStamp: LocalDateTime?= LocalDateTime.now(),
    val chatId: String
)
