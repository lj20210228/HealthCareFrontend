package com.example.mobilehealthcare.service

import com.example.mobilehealthcare.domain.Recipe
import com.example.mobilehealthcare.models.response.ListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface RecipeService {
    @GET("/recipe/patient/{patientId}")
    suspend fun getAllRecipesForPatient(@Path("patientId") patientId: String): Response<ListResponse<Recipe>>
}