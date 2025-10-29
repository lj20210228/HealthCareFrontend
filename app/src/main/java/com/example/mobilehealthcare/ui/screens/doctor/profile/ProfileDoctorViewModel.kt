package com.example.mobilehealthcare.ui.screens.doctor.profile

import android.util.Log
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilehealthcare.domain.Doctor
import com.example.mobilehealthcare.domain.Hospital
import com.example.mobilehealthcare.domain.User
import com.example.mobilehealthcare.domain.WorkTime
import com.example.mobilehealthcare.models.response.BaseResponse
import com.example.mobilehealthcare.models.response.ListResponse
import com.example.mobilehealthcare.service.DoctorService
import com.example.mobilehealthcare.service.HospitalService
import com.example.mobilehealthcare.service.TokenStorage
import com.example.mobilehealthcare.service.UserService
import com.example.mobilehealthcare.service.WorkTimeService
import com.example.mobilehealthcare.ui.screens.doctor.profile.ProfileDoctorUiState.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileDoctorViewModel @Inject constructor(val hospitalService: HospitalService,
    val tokenStorage: TokenStorage,val doctorService: DoctorService,val workTimeService: WorkTimeService,
    val userService: UserService
    ): ViewModel() {
        private val _uiState= MutableStateFlow<ProfileDoctorUiState>(ProfileDoctorUiState.Nothing)
    val uiState=_uiState.asStateFlow()
        val hospitalId=tokenStorage.getHospitalId()
    val doctorId=tokenStorage.getDoctorId()
    val userId=tokenStorage.getUserId()


    init {
        viewModelScope.launch {
            getUserById()
            getHospitalById()
            getDoctorById()
            getWorkTimeForDoctor()
        }

    }

    fun getUserById(){
        viewModelScope.launch {
            Log.d("UserId",userId.toString())
            val response=userService.getUserById(userId!!)
            val body=response.body()
            Log.d("User",response.body().toString())

            when(body){
                is BaseResponse.SuccessResponse->{
                    updateState { it.copy(user = body.data) }
                }
                is BaseResponse.ErrorResponse->{
                    _uiState.value= Error(message = body.exception)
                }
                null->_uiState.value= Error(message = "Nema odgovora sa servera")
            }
        }
    }
    fun getDoctorById(){
        viewModelScope.launch {
            Log.d("DoctorId",doctorId.toString())

            val response=doctorService.getDoctorForId(doctorId!!)
            val body=response.body()
            Log.d("Doctor",response.body().toString())
            when(body){
                is BaseResponse.SuccessResponse->{
                    updateState { it.copy(doctor = body.data) }
                }
                is BaseResponse.ErrorResponse->{
                    _uiState.value= Error(message = body.exception)
                }
                null->_uiState.value= Error(message = "Nema odgovora sa servera")
            }
        }
    }
    fun getWorkTimeForDoctor(){
        viewModelScope.launch {

            val response=workTimeService.getWorkTimeForDoctorId(doctorId!!)
            val body=response.body()
            Log.d("WorkTime",response.body().toString())

            when(body){
                is ListResponse.SuccessResponse->{
                    updateState { it.copy(workTime = body.data as List<WorkTime>) }
                }
                is ListResponse.ErrorResponse->{
                    _uiState.value= Error(message = body.exception)
                }
                null->_uiState.value= Error(message = "Nema odgovora sa servera")
            }
        }
    }
     fun getHospitalById(){
         viewModelScope.launch {
             Log.d("HospitalId",hospitalId.toString())

             val response=hospitalService.getHospitalById(hospitalId!!)

             val body=response.body()
             Log.d("Hospital",response.body().toString())
             when(body){
                 is BaseResponse.ErrorResponse->{
                     _uiState.value= Error(message = body.exception.toString())
                 }
                 is BaseResponse.SuccessResponse->{
                     updateState { it.copy(hospital = body.data) }
                 }

                 null -> Error(message = "Nema odgovora sa servera")
             }

         }

    }
    fun logout(){
        tokenStorage.clearToken()
        tokenStorage.clearDoctorId()
        tokenStorage.clearUserId()
        tokenStorage.clearRole()
        tokenStorage.clearHospitalId()
    }
    private fun updateState(update: (ProfileDoctorUiState.Success) -> ProfileDoctorUiState.Success) {
        val current = _uiState.value
        if (current is ProfileDoctorUiState.Success) {
            _uiState.value = update(current)
        } else {
            _uiState.value = update(ProfileDoctorUiState.Success())
        }
    }


}
sealed class ProfileDoctorUiState{
    object Nothing:ProfileDoctorUiState()
    object Loading: ProfileDoctorUiState()
    data class Success(val hospital: Hospital?=null, val doctor: Doctor?=null, val user: User?=null, val workTime:List<WorkTime> =emptyList()):
        ProfileDoctorUiState()
    data class Error(val message: String?=null): ProfileDoctorUiState()
}