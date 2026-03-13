package com.example.mobilehealthcare.service

import com.example.mobilehealthcare.domain.Message
import com.example.mobilehealthcare.models.response.BaseResponse
import com.example.mobilehealthcare.models.response.ListResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MessageService {
    @GET("/messages/chat/{id}")
    suspend fun getAllMessagesInChat(@Path("id")id: String): Response<ListResponse<Message>>
    @POST("/messages/add")
    suspend fun sentMessage(@Body message: Message): Response<BaseResponse<Message>>


}