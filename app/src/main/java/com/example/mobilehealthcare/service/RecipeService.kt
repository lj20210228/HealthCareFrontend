package com.example.mobilehealthcare.service

import com.example.mobilehealthcare.domain.Recipe
import com.example.mobilehealthcare.models.response.BaseResponse
import com.example.mobilehealthcare.models.response.ListResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RecipeService {
    @GET("/recipe/patient/{patientId}")
    suspend fun getAllRecipesForPatient(@Path("patientId") patientId: String): Response<ListResponse<Recipe>>
    @GET("/recipe/patient/valid/{patientId}")
    suspend fun getAllValidRecipesForPatient(@Path("patientId") patientId: String): Response<ListResponse<Recipe>>
    @POST("/recipe/add")
    suspend fun addRecipe(@Body recipe: Recipe): Response<BaseResponse<Recipe>>
}