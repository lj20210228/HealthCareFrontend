package com.example.mobilehealthcare.domain

import com.example.mobilehealthcare.date.StrictLocalTimeSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalTime
@Serializable
data class WorkTime(
    val id: String,
    @Serializable(with = StrictLocalTimeSerializer::class)
    val startTime: LocalTime,
    @Serializable(with = StrictLocalTimeSerializer::class)
    val endTime: LocalTime,
    val doctorId: String,
    val dayIn: DayInWeek
)