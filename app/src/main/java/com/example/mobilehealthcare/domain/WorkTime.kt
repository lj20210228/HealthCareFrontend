package com.example.mobilehealthcare.domain

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalTime
@Serializable
data class WorkTime(
    val id: String,
    @Contextual
    val startTime: LocalTime,
    @Contextual
    val endTime: LocalTime,
    val doctorId: String,
    val dayInWeek: DayInWeek
)