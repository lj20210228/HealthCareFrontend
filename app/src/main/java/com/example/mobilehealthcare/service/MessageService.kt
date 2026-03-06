package com.example.mobilehealthcare.service

import com.example.mobilehealthcare.domain.Message
import com.example.mobilehealthcare.models.response.ListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface MessageService {
    @GET("/messages/chat/{id}")
    suspend fun getAllMessagesInChat(@Path("id")id: String): Response<ListResponse<Message>>

}