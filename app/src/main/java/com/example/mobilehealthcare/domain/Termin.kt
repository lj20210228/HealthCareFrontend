package com.example.mobilehealthcare.domain

import android.os.Build
import android.os.Parcelable
import androidx.annotation.RequiresApi
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalTime
@RequiresApi(Build.VERSION_CODES.O)
@Serializable
@Parcelize
data class Termin @RequiresApi(Build.VERSION_CODES.O) constructor(
    val id: String?=null,
    val doctorId: String?=null,
    val patientId:String?=null,
    @Contextual
    val date: LocalDate,
    @Contextual
    val startTime: LocalTime,
    @Contextual
    val endTime: LocalTime?=startTime.plusHours(1),
    val hospitalId: String?=null,
    val status: TerminStatus= TerminStatus.PENDING,
    val desctiption: String?="Kontrola"
): Parcelable