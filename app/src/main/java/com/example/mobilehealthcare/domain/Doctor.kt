package com.example.mobilehealthcare.domain

data class Doctor(
    val id: String,
    val  userId: String,
    val fullName:String,
    val specialization: String,
    val maxPatients:Int,
    val currentPatients:Int=0,
    val hospitalId: String,
    val isGeneral: Boolean=false
)