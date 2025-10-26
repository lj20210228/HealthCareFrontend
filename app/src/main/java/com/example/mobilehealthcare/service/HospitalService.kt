package com.example.mobilehealthcare.service

import com.example.mobilehealthcare.domain.Hospital
import com.example.mobilehealthcare.models.response.BaseResponse
import com.example.mobilehealthcare.models.response.ListResponse
import retrofit2.Response
import retrofit2.http.GET

interface HospitalService {
    @GET("/hospitals")
    suspend fun getAllHospitals(): Response<ListResponse<Hospital>>
    @GET("/hospitals/{id}")
    suspend fun getHospitalById(id: String): Response<BaseResponse<Hospital>>
}