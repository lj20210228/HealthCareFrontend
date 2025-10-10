package com.example.mobilehealthcare.domain

import java.time.LocalTime

data class WorkTime(
    val id: String,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val doctorId: String,
    val dayInWeek: DayInWeek
)