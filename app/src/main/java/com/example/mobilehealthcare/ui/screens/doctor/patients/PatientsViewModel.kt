package com.example.mobilehealthcare.ui.screens.doctor.patients

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilehealthcare.domain.Patient
import com.example.mobilehealthcare.domain.Recipe
import com.example.mobilehealthcare.models.response.BaseResponse
import com.example.mobilehealthcare.models.response.ListResponse
import com.example.mobilehealthcare.service.RecipeService
import com.example.mobilehealthcare.service.SelectedDoctorService
import com.example.mobilehealthcare.service.TokenStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PatientsViewModel @Inject constructor  (

    val selectedDoctorService: SelectedDoctorService,
    val tokenStorage: TokenStorage,
    val recipeService: RecipeService,
): ViewModel() {
    val doctorId=tokenStorage.getDoctorId()
    private val _uiState= MutableStateFlow<PatientsForDoctorUiState>(PatientsForDoctorUiState())
    val uiState=_uiState.asStateFlow()


    init {
        getAllPatientsForDoctor()
        _uiState.update { it.copy(doctorId=doctorId!!)}
    }

    fun getAllPatientsForDoctor(){
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val response=selectedDoctorService.getPatientsForSelectedDoctor(doctorId!!)
            Log.d("Response for patients",response.toString())
            val responseBody=response.body()
            val patientsWithRecipe=mutableListOf<PatientWithRecipe>()
            if (responseBody is ListResponse.SuccessResponse){
                responseBody.data?.mapNotNull { patient->
                    val recipeResponse=recipeService.getAllRecipesForPatient(patient?.id!!)
                    val body=recipeResponse.body()
                    if(body is ListResponse.SuccessResponse){
                        patientsWithRecipe.add(
                            PatientWithRecipe(
                                patient=patient,
                                recipes= body.data!! as List<Recipe>
                            )
                        )
                    }
                 }

                _uiState.update { it.copy(isLoading = false, patients =  patientsWithRecipe) }
            }
            if(responseBody is ListResponse.ErrorResponse){
                _uiState.update { it.copy(isLoading = true, error = responseBody.message) }

            }
            _uiState.update { it.copy(isLoading = false) }

        }
    }
    fun addRecipe(recipe: Recipe){
        viewModelScope.launch {
            Log.d("Recipe",recipe.toString())
            _uiState.update { it.copy(isLoading = true) }
            val response=recipeService.addRecipe(recipe)
            Log.d("RecipeAdded",recipeService.addRecipe(recipe).toString())
            if (response.body() is BaseResponse.SuccessResponse){
                _uiState.update { it.copy(isLoading = false) }

            }
            if (response.body() is BaseResponse.ErrorResponse){
                _uiState.update { it.copy(isLoading = false, error = response.body()?.statusCode.toString()) }

            }
            _uiState.update { it.copy(isLoading = false) }

        }
    }




}

data class PatientsForDoctorUiState(
    val error:String?=null,
    val patients:List<PatientWithRecipe> =emptyList(),
    val isLoading: Boolean=false,
    val doctorId: String?=null
)
data class PatientWithRecipe(
    val patient: Patient,
    val recipes:List<Recipe>
)