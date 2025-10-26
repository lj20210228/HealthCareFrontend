package com.example.mobilehealthcare.service

import com.example.mobilehealthcare.domain.User
import com.example.mobilehealthcare.models.response.BaseResponse
import retrofit2.Response
import retrofit2.http.GET

interface UserService {
    @GET("users/{id}")
    suspend fun getUserById(): Response<BaseResponse<User>>
}