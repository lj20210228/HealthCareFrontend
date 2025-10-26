package com.example.mobilehealthcare.service

import com.example.mobilehealthcare.domain.WorkTime
import com.example.mobilehealthcare.models.response.BaseResponse
import com.example.mobilehealthcare.models.response.ListResponse
import retrofit2.Response
import retrofit2.http.GET

interface WorkTimeService {
    @GET("/worktime/doctor/{id}")
    suspend fun getWorkTimeForDoctorId(): Response<ListResponse<WorkTime>>
}