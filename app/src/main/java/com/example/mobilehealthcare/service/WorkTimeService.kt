package com.example.mobilehealthcare.service

import com.example.mobilehealthcare.domain.WorkTime
import com.example.mobilehealthcare.models.response.BaseResponse
import com.example.mobilehealthcare.models.response.ListResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface WorkTimeService {
    @GET("/worktime/doctor/{id}")
    suspend fun getWorkTimeForDoctorId(@Path("id") id: String): Response<ListResponse<WorkTime>>
    @POST("/worktime/add")
    suspend fun addWorkTime(@Body workTime: WorkTime): Response<BaseResponse<WorkTime>>
    @PUT("/worktime/update")
    suspend fun updateWorkTime(@Body workTime: WorkTime): Response<BaseResponse<WorkTime>>
    @DELETE("/worktime/delete/{id}")
    suspend fun deleteWorkTime(@Path("id") id:String): Response<BaseResponse<Boolean>>

}