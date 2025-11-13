package com.example.mobilehealthcare.service

import com.example.mobilehealthcare.domain.Patient
import com.example.mobilehealthcare.models.response.BaseResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface PatientService {
    @GET("/patients/{id}")
    suspend fun getPatient(@Path("id") id: String): Response<BaseResponse<Patient>>
    @GET("/patient/userId/{userId}")
    suspend fun getPatientByUserId(@Path("userId") userId: String): Response<BaseResponse<Patient>>



}