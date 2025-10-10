package com.example.mobilehealthcare.domain

import java.time.LocalDate
import java.time.LocalTime

data class Termin(
    val id: String,
    val doctorId: String,
    val patientId:String,
    val date: LocalDate
    ,val startTime: LocalTime,
    val endTime: LocalTime,
    val hospitalId: String,
    val status: TerminStatus= TerminStatus.PENDING
)