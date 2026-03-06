package com.example.mobilehealthcare.service

import com.example.mobilehealthcare.domain.Chat
import com.example.mobilehealthcare.models.response.BaseResponse
import com.example.mobilehealthcare.models.response.ListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ChatService {
    @GET("/chat/patient/{id}")
    suspend fun getAllChatsForPatient(@Path("id")patientId: String): Response<ListResponse<Chat>>
    @GET("/chat/doctor/{id}")
    suspend fun getAllChatsForDoctor(@Path("id")doctorId: String): Response<ListResponse<Chat>>
    @POST("/chat/add")
    suspend fun addChat(chat: Chat): Response<BaseResponse<Chat>>
}