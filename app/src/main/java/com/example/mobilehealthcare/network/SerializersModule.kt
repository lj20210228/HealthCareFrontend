package com.example.mobilehealthcare.network

import com.example.mobilehealthcare.domain.Doctor
import com.example.mobilehealthcare.domain.Hospital
import com.example.mobilehealthcare.domain.Patient
import com.example.mobilehealthcare.domain.User
import com.example.mobilehealthcare.models.request.RegisterRequest
import com.example.mobilehealthcare.models.response.BaseResponse
import com.example.mobilehealthcare.models.response.RegisterResponse
import kotlinx.serialization.KSerializer
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import kotlinx.serialization.serializer
import kotlin.reflect.KClass

val appSerializersModule = SerializersModule {


    polymorphic(
        Any::class,
        actualClass = RegisterResponse::class,
        actualSerializer = RegisterResponse.serializer()
    )

    polymorphic(
        Any::class,
        actualClass = RegisterRequest::class,
        actualSerializer = RegisterRequest.serializer()
    )
    polymorphic(
        Any::class,
        actualClass = User::class,
        actualSerializer = User.serializer()
    )

    polymorphic(
        Any::class,
        actualClass = Doctor::class,
        actualSerializer = Doctor.serializer()
    )
    polymorphic(
        Any::class,
        actualClass = Patient::class,
        actualSerializer = Patient.serializer()
    )
    polymorphic(
        Any::class,
        actualClass = Hospital::class,
        actualSerializer = Hospital.serializer()
    )


}

