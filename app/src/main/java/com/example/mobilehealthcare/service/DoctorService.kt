package com.example.mobilehealthcare.service

import com.example.mobilehealthcare.domain.Doctor
import com.example.mobilehealthcare.models.response.BaseResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface DoctorService {

    @GET("/doctor/{id}")
    suspend fun getDoctorForId(@Path("id") id: String): Response<BaseResponse<Doctor>>
}