package com.example.mobilehealthcare.service


import com.example.mobilehealthcare.domain.Hospital
import com.example.mobilehealthcare.models.request.LoginRequest
import com.example.mobilehealthcare.models.request.RegisterRequest
import com.example.mobilehealthcare.models.response.BaseResponse
import com.example.mobilehealthcare.models.response.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthService {

    @POST("/register")
    suspend fun register(@Body registerParams: RegisterRequest): Response<BaseResponse<RegisterResponse>>
    @POST("/login")
    suspend fun login(@Body request: LoginRequest): Response<BaseResponse<RegisterResponse>>

}