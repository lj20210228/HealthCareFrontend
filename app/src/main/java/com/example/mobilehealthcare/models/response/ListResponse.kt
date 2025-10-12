package com.example.mobilehealthcare.models.response

import io.ktor.http.HttpStatusCode
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class ListResponse< out T>(
    open val statusCode: Int= HttpStatusCode.OK.value
) {

    @Serializable
    @SerialName("success")
    data class SuccessResponse< T>(
        val data: @Serializable List<T?>?=null,
        val message: String? = null,
    ) : ListResponse< T>(HttpStatusCode.OK.value)




    @Serializable
    @SerialName("error")
    data class ErrorResponse<T>(
        val exception: String?= null,
        val message: String? = null,

        ) : ListResponse<T>(HttpStatusCode.BadRequest.value)

}