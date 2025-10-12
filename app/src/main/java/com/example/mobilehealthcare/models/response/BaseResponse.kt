package com.example.mobilehealthcare.models.response

import io.ktor.http.HttpStatusCode
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable

sealed class BaseResponse< T>(
    open val statusCode: Int= HttpStatusCode.OK.value
) {


    @Serializable
    @SerialName("success")
    data class SuccessResponse<T>(
        val data:@Serializable T? = null,
        val message: String? = null,
    ): BaseResponse<T>(HttpStatusCode.OK.value)


    @Serializable
    @SerialName("error")
    data class ErrorResponse<T>(
        val exception: String?= null,
        val message: String? = null,

        ) : BaseResponse<T>(HttpStatusCode.BadRequest.value)

}