package com.example.mobilehealthcare.service

import com.example.mobilehealthcare.domain.Termin
import com.example.mobilehealthcare.models.response.BaseResponse
import com.example.mobilehealthcare.models.response.ListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface TerminService {
    @GET("/termin/patient/{id}")
    suspend fun getAllTerminsForPatient(@Path("id") id: String): Response<ListResponse<Termin>>

    @GET("/termin/doctor/{id}")
    suspend fun getAllTerminsForDoctor(@Path("id") id: String): Response<ListResponse<Termin>>



}