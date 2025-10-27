package com.example.mobilehealthcare.service

import com.example.mobilehealthcare.domain.User
import com.example.mobilehealthcare.models.response.BaseResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface UserService {
    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: String): Response<BaseResponse<User>>
}