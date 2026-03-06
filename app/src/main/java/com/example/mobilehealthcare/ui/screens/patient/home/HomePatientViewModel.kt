package com.example.mobilehealthcare.ui.screens.patient.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobilehealthcare.domain.Patient
import com.example.mobilehealthcare.domain.Recipe
import com.example.mobilehealthcare.domain.Termin
import com.example.mobilehealthcare.models.response.BaseResponse
import com.example.mobilehealthcare.models.response.ListResponse
import com.example.mobilehealthcare.service.DoctorService
import com.example.mobilehealthcare.service.PatientService
import com.example.mobilehealthcare.service.RecipeService
import com.example.mobilehealthcare.service.TerminService
import com.example.mobilehealthcare.service.TokenStorage
import com.example.mobilehealthcare.service.UserService
import com.example.mobilehealthcare.ui.screens.patient.recipes.RecipeWithDoctorName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomePatientViewModel @Inject constructor (
    val patientService: PatientService,
    val terminService: TerminService,
    val tokenStorage: TokenStorage,
    val recipeService: RecipeService,
    val doctorService: DoctorService

): ViewModel(){
    private val _uiState= MutableStateFlow(HomePatientUiState())
    val uiState=_uiState.asStateFlow()
    val userId=tokenStorage.getUserId()


    init {
        loadPatient()
    }
    private fun loadPatient(){
        viewModelScope.launch {

            try {
                _uiState.update { it.copy(isLoading = true) }

                val patient=patientService.getPatientByUserId(userId!!)
                Log.d("Patient",patient.body().toString())

                val patientResponse=patient.body()

                when(patientResponse){
                    is BaseResponse.ErrorResponse->{
                        _uiState.update { it.copy(error=patientResponse.message) }
                        return@launch
                    }
                    is BaseResponse.SuccessResponse->{
                        _uiState.update { it.copy(patient=patientResponse.data) }
                        tokenStorage.savePatientId(patientResponse.data?.id!!)
                        Log.d("SavePatientId",tokenStorage.getPatientId()!!)

                        val termins=terminService.getAllTerminsForPatient(patientResponse.data?.id!!)
                        Log.d("Termins",termins.toString())
                        val recipes=recipeService.getAllRecipesForPatient(patientResponse.data.id)
                        Log.d("Recipes",recipes.toString())
                        val terminsResponse=termins.body()
                        val recipesResponse=recipes.body()
                        if (terminsResponse is ListResponse.SuccessResponse){
                            val terminsWithDoctorName=mutableListOf<TerminWithDoctorName>()
                            terminsResponse.data?.mapNotNull {termin->
                                val doctorResponse=doctorService.getDoctorForId(termin?.doctorId!!).body()
                                if (doctorResponse is BaseResponse.SuccessResponse){
                                    terminsWithDoctorName.add(
                                        TerminWithDoctorName(
                                            termin=termin,
                                            doctorName = doctorResponse.data?.fullName!!
                                        )
                                    )
                                }

                            }
                            _uiState.update { it.copy(termins = terminsWithDoctorName) }
                        }else if(terminsResponse is ListResponse.ErrorResponse){
                            _uiState.update { it.copy(termins=emptyList()) }
                        }
                        if (recipesResponse is ListResponse.SuccessResponse){
                            val recipesWithDoctorName=mutableListOf<RecipeWithDoctorName>()
                            recipesResponse.data?.mapNotNull {
                                val doctorResponse=doctorService.getDoctorForId(it?.doctorId!!).body()
                                if (doctorResponse is BaseResponse.SuccessResponse){
                                    recipesWithDoctorName.add(
                                        RecipeWithDoctorName(
                                            recipe=it,
                                            doctorName = doctorResponse.data?.fullName!!
                                        )
                                    )
                                }
                            }
                            _uiState.update {it.copy(recipes=recipesWithDoctorName) }

                        }else if(recipesResponse is ListResponse.ErrorResponse){
                            _uiState.update { it.copy(recipes=emptyList()) }
                        }

                    }

                    null -> _uiState.update { it.copy(error="Pacijent nije pronadjen", isLoading = false) }
                }

                _uiState.update {
                    it.copy(isLoading = false)
                }
            }catch (e: Exception){

                _uiState.update {
                    it.copy(
                        isLoading = false
                        , error = e.localizedMessage?:"Greska pri ucitavanju"
                    )
                }
            }
            Log.d("UIState",_uiState.value.toString())

        }
    }
    fun refreshData(){
        loadPatient()
    }

}
data class HomePatientUiState(
    val isLoading: Boolean=true,
    val patient: Patient?=null,
    val termins: List<TerminWithDoctorName> =emptyList(),
    val recipes:List<RecipeWithDoctorName> =emptyList(),
    val error:String?=null
)
data class TerminWithDoctorName(
    val termin: Termin,
    val doctorName: String
)