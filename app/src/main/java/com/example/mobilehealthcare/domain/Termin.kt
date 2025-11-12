package com.example.mobilehealthcare.domain

import android.os.Build
import android.os.Parcelable
import androidx.annotation.RequiresApi
import com.example.mobilehealthcare.date.StrictLocalDateSerializer
import com.example.mobilehealthcare.date.StrictLocalTimeSerializer
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
    @Serializable(with= StrictLocalDateSerializer::class)
    val date: LocalDate,
    @Serializable(with= StrictLocalTimeSerializer::class)
    val startTime: LocalTime,
    @Serializable(with= StrictLocalTimeSerializer::class)
    val endTime: LocalTime?=startTime.plusHours(1),
    val hospitalId: String?=null,
    val status: TerminStatus= TerminStatus.PENDING,
    val desctiption: String?="Kontrola"
): Parcelable