package com.example.mobilehealthcare.ui.screens.patient.doctors

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilehealthcare.domain.Doctor
import com.example.mobilehealthcare.domain.Hospital
import com.example.mobilehealthcare.domain.Patient
import com.example.mobilehealthcare.domain.Recipe
import com.example.mobilehealthcare.domain.SelectedDoctor
import com.example.mobilehealthcare.domain.Termin
import com.example.mobilehealthcare.models.response.BaseResponse
import com.example.mobilehealthcare.models.response.ListResponse
import com.example.mobilehealthcare.service.DoctorService
import com.example.mobilehealthcare.service.HospitalService
import com.example.mobilehealthcare.service.SelectedDoctorService
import com.example.mobilehealthcare.service.TerminService
import com.example.mobilehealthcare.service.TokenStorage
import com.example.mobilehealthcare.ui.screens.shared.AuthStatusViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DoctorScreenForPatientViewModel @Inject constructor(
    val tokenStorage: TokenStorage,
    val selectedDoctorService: SelectedDoctorService,
    val doctorService: DoctorService,
    val hospitalService: HospitalService,
    val terminService: TerminService,
    val authStatusViewModel: AuthStatusViewModel
): ViewModel() {
    val patientId=tokenStorage.getPatientId()
    private val _uiState= MutableStateFlow(HomePatientUiState())
    val uiState=_uiState.asStateFlow()
    val hospitalId=tokenStorage.getHospitalId()


    init {
        getSelectedDoctorForPatient()
        getHospital()
        getAllDoctorsInHospital()
    }
    fun getSelectedDoctorForPatient(){
        viewModelScope.launch {
            Log.d("PatientId",patientId.toString())
            if (patientId==null)
            {
                _uiState.update { it.copy(error="Greška pri učitavanju") }
                return@launch
            }

            _uiState.update { it.copy(isLoading = true) }
            val doctorsResponse=selectedDoctorService.getAllSelectedDoctorsForPatint(patientId)
            val doctors=doctorsResponse.body()
            if (doctors is ListResponse.SuccessResponse){
                val doctorTerminList=doctors.data?.mapNotNull { doctor->
                    doctor?.let {
                        val terminsResponse=terminService.getAllTerminsForDoctor(doctor.id!!)
                        val termins=terminsResponse.body()
                        Log.d("Termins",termins.toString())

                        val terminList=if (termins is ListResponse.SuccessResponse){
                            termins.data?:emptyList()
                        }else emptyList()
                        Pair(doctor,terminList)


                    }
                }?:emptyList()

                _uiState.update { it.copy(isLoading = false, myDoctors =doctorTerminList ) }
            }else if(doctors is ListResponse.ErrorResponse){
                _uiState.update{it.copy(myDoctors = emptyList(), error = doctors.message, isLoading = false)}
            }

        }
    }
    fun getAllDoctorsInHospital(){
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            if (hospitalId==null)
            {
                _uiState.update { it.copy(isLoading = false,error="Greska pri ucitavanju") }
                return@launch
            }
            val doctorsResponse=doctorService.getAllDoctorsInHospital(hospitalId)
            Log.d("AllDoctors",doctorsResponse.toString())

            val doctors=doctorsResponse.body()
            Log.d("AllDoctors",doctors.toString())

            if (doctors is ListResponse.SuccessResponse){
                val doctorTerminList=doctors.data?.mapNotNull { doctor->
                    doctor?.let {
                        val terminsResponse=terminService.getAllTerminsForDoctor(doctor.id!!)
                        val termins=terminsResponse.body()
                        Log.d("Termins",termins.toString())

                        val terminList=if (termins is ListResponse.SuccessResponse){
                            termins.data?:emptyList()
                        }else emptyList()
                        Pair(doctor,terminList)


                    }
                }?:emptyList()

                _uiState.update { it.copy(isLoading = false, allDoctors =doctorTerminList ) }
            }else if(doctors is ListResponse.ErrorResponse){
                _uiState.update{it.copy(myDoctors = emptyList(), error = doctors.message, isLoading = false)}
            }

        }
    }
    fun addSelectedDoctor(selectedDoctor: SelectedDoctor){
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val response=selectedDoctorService.addSelectedDoctor(selectedDoctor)
            val responseBody=response.body()
            if (responseBody is BaseResponse.SuccessResponse){

                getSelectedDoctorForPatient()
            }
            if (responseBody is BaseResponse.ErrorResponse){
                _uiState.update { it.copy(isLoading = false,error=responseBody.message ) }

            }

        }
    }
    fun getHospital() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            Log.d("HospitalId",hospitalId.toString())

            if (hospitalId==null)
            {
                _uiState.update { it.copy(error = "Greska pri ucitavanju") }
                return@launch
            }
            val response=hospitalService.getHospitalById(hospitalId)
            val responseBody=response.body()
            if (responseBody is BaseResponse.SuccessResponse)
            {
                _uiState.update { it.copy(hospital = responseBody.data, isLoading = false) }
            }else if(responseBody is BaseResponse.ErrorResponse){
                _uiState.update { it.copy(error = responseBody.message, isLoading = false) }

            }
            Log.d("Hospital",_uiState.value.hospital.toString())


        }
    }
    fun logout(){
        tokenStorage.clearToken()
        tokenStorage.clearPatientId()
        tokenStorage.clearUserId()
        tokenStorage.clearRole()
        tokenStorage.clearHospitalId()
        authStatusViewModel.updateAuthStatus()

    }
}

data class HomePatientUiState(
    val isLoading: Boolean=true,
    val myDoctors: List<Pair<Doctor, List<Termin?>>> =emptyList(),
    val allDoctors: List<Pair<Doctor,List<Termin?>>> =emptyList(),
    val hospital: Hospital?=null,
    val error:String?=null
)