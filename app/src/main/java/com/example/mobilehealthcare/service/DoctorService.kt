package com.example.mobilehealthcare.service

import com.example.mobilehealthcare.domain.Doctor
import com.example.mobilehealthcare.models.response.BaseResponse
import retrofit2.Response
import retrofit2.http.GET

interface DoctorService {

    @GET("/doctor/{id}")
    suspend fun getDoctorForId(): Response<BaseResponse<Doctor>>
}