package com.example.mobilehealthcare.domain

data class User(
    val email:String,
    val password:String,
    val role:Role,
    val id: String
)
