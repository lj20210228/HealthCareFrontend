package com.example.mobilehealthcare.ui.screens.doctor.home

import android.util.Log
import androidx.compose.ui.res.integerResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilehealthcare.domain.Doctor
import com.example.mobilehealthcare.domain.Patient
import com.example.mobilehealthcare.domain.Termin
import com.example.mobilehealthcare.models.response.BaseResponse
import com.example.mobilehealthcare.models.response.ListResponse
import com.example.mobilehealthcare.service.DoctorService
import com.example.mobilehealthcare.service.PatientService
import com.example.mobilehealthcare.service.TerminService
import com.example.mobilehealthcare.service.TokenStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.emptyList
@HiltViewModel
class HomeDoctorViewModel @Inject constructor(
    val terminService: TerminService,
    val tokenStorage: TokenStorage,
    val doctorService: DoctorService,
    val patientService: PatientService
): ViewModel() {
    val doctorId=tokenStorage.getDoctorId()
    private val _uiState= MutableStateFlow(DoctorHomeUiState())
    val uiState=_uiState.asStateFlow()

    init {
        getDoctor()
        getTerminsWithPatients()
    }
    fun getDoctor(){
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            if (doctorId==null)
            {
                _uiState.update { it.copy(isLoading = false, error = "Neuspesno učitavanje") }
                return@launch
            }

            val response=doctorService.getDoctorForId(doctorId).body()
            Log.d("Doctor",response.toString())
            if (response is BaseResponse.SuccessResponse){
                _uiState.update { it.copy(isLoading = false, doctor = response.data) }
            }
            if (response is BaseResponse.ErrorResponse){
                _uiState.update { it.copy(isLoading = false, error = response.message?:"Neuspesno učitavanje") }
            }

        }
    }
    fun getTerminsWithPatients(){
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            if (doctorId==null)
            {
                _uiState.update { it.copy(isLoading = false, error = "Neuspesno učitavanje") }
                return@launch
            }
            val terminResponse=terminService.getAllTerminsForDoctor(doctorId).body()
            Log.d("Termin",terminResponse.toString())

            if (terminResponse is ListResponse.ErrorResponse){
                _uiState.update { it.copy(isLoading = false, error = terminResponse.message) }

            }
            if (terminResponse is ListResponse.SuccessResponse){
                val termins=terminResponse.data?:emptyList()
                val terminsWithPatients=termins.mapNotNull { termin->
                    termin?.let {
                        val patientResponse=patientService.getPatient(termin.patientId!!).body()
                        Log.d("PatientResponse",patientResponse.toString())

                        when (patientResponse) {
                            is BaseResponse.SuccessResponse -> {
                                val patient = patientResponse.data
                                if (patient != null) Pair(it, patient) else null
                            }
                            is BaseResponse.ErrorResponse -> {
                                _uiState.update { state ->
                                    state.copy(isLoading = false, error = patientResponse.message)
                                }
                                null
                            }
                            else -> null
                        }
                    }


                }
                _uiState.update { it.copy(isLoading = false, terminsWithPatient = terminsWithPatients) }
            }



        }
    }
    fun updateTerminRequest(termin: Termin) {
        viewModelScope.launch {
            Log.d("Update",termin.toString())

            val terminResponse=terminService.updateTermin(termin)
            val termin=terminResponse.body()
            Log.d("Update",terminResponse.toString())

            if (termin is BaseResponse.SuccessResponse){
                getTerminsWithPatients()
            }
            if (termin is BaseResponse.ErrorResponse){
                _uiState.update { it.copy(error=termin.message) }
            }

        }
    }
    fun deleteTermin(terminId:String){
        viewModelScope.launch {
            val terminResponse=terminService.deleteTermin(terminId)
            val termin=terminResponse.body()
            Log.d("Delete",terminResponse.toString())
            if (termin is BaseResponse.SuccessResponse){
                getTerminsWithPatients()
            }
            if (termin is BaseResponse.ErrorResponse){
                _uiState.update { it.copy(error=termin.message) }
            }        }
    }
}
data class DoctorHomeUiState(
    val doctor: Doctor?=null,
    val terminsWithPatient: List<Pair<Termin, Patient>> = emptyList(),
    val error:String?=null,
    val isLoading: Boolean=false,
)