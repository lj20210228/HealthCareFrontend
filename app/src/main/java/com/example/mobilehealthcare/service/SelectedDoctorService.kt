package com.example.mobilehealthcare.service

import com.example.mobilehealthcare.domain.Doctor
import com.example.mobilehealthcare.domain.Patient
import com.example.mobilehealthcare.domain.SelectedDoctor
import com.example.mobilehealthcare.models.response.BaseResponse
import com.example.mobilehealthcare.models.response.ListResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface SelectedDoctorService {


    @GET("selectedDoctors/doctors/{patientId}")
    suspend fun getAllSelectedDoctorsForPatint(@Path("patientId") patientId: String): Response<ListResponse<Doctor>>
    @POST("selectedDoctors")
    suspend fun addSelectedDoctor(@Body selectedDoctor: SelectedDoctor): Response<BaseResponse<SelectedDoctor>>
    @GET("selectedDoctors/patients/{doctorId}")
    suspend fun getPatientsForSelectedDoctor(@Path("doctorId")doctorId: String): Response<ListResponse<Patient>>
}