package com.example.mobilehealthcare.domain

data class Patient(
    val id: String,
    val userId:String,
    val fullName: String,
    val hospitalId: String,
    val jmbg: String
)