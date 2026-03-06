package com.example.mobilehealthcare.ui.screens.patient.recipes

import android.util.Log
import android.view.View
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilehealthcare.domain.Recipe
import com.example.mobilehealthcare.models.response.BaseResponse
import com.example.mobilehealthcare.models.response.ListResponse
import com.example.mobilehealthcare.service.DoctorService
import com.example.mobilehealthcare.service.RecipeService
import com.example.mobilehealthcare.service.TokenStorage
import com.example.mobilehealthcare.ui.screens.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    val recipeService: RecipeService,
    val tokenStorage: TokenStorage,
    val doctorService: DoctorService
): ViewModel() {
    private val _uiState= MutableStateFlow<RecipeUiState>(RecipeUiState())
    val uiState=_uiState.asStateFlow()
    val patientId=tokenStorage.getPatientId()
    init {
        getAllValidRecipes()
        getAllExpiredRecipes()
    }


    fun getAllValidRecipes(){
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val response=recipeService.getAllValidRecipesForPatient(patientId!!)
            Log.d("Active recipes",response.toString())
            val body=response.body()
            if (body is ListResponse.SuccessResponse){
                var recipesWithDoctor=mutableListOf<RecipeWithDoctorName>()
                body.data?.mapNotNull {recipe->
                    val responseDoctor=doctorService.getDoctorForId(recipe?.doctorId!!).body()
                    if(responseDoctor is BaseResponse.SuccessResponse){
                        recipesWithDoctor.add(
                            RecipeWithDoctorName(
                                doctorName = responseDoctor.data?.fullName!!,
                                recipe = recipe
                            )
                        )
                    }


                }
                _uiState.update { it.copy(isLoading = false, activeRecipes =recipesWithDoctor) }



            }
            if (body is ListResponse.ErrorResponse){
                _uiState.update { it.copy(isLoading = false, error = body.message) }
            }
            _uiState.update { it.copy(isLoading = false) }

        }
    }
    fun getAllExpiredRecipes() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val response = recipeService.getAllRecipesForPatient(patientId!!)
            val body = response.body()

            if (body is ListResponse.SuccessResponse) {
                val recipesWithDoctor = mutableListOf<RecipeWithDoctorName>()

                val expiredRecipes = body.data?.filter {
                    it?.dateExpired?.isBefore(LocalDate.now()) == true
                } ?: emptyList()

                expiredRecipes.forEach { recipe ->
                    recipe?.let {
                        val responseDoctor = doctorService.getDoctorForId(it.doctorId).body()
                        if (responseDoctor is BaseResponse.SuccessResponse) {
                            recipesWithDoctor.add(
                                RecipeWithDoctorName(
                                    doctorName = responseDoctor.data?.fullName ?: "Nepoznat lekar",
                                    recipe = it
                                )
                            )
                        }
                    }
                }

                _uiState.update { it.copy(isLoading = false, recipes = recipesWithDoctor) }
            } else if (body is ListResponse.ErrorResponse) {
                _uiState.update { it.copy(isLoading = false, error = body.message) }
            }

            _uiState.update { it.copy(isLoading = false) }
        }
    }
}
data class RecipeUiState(
    val recipes: List<RecipeWithDoctorName> =emptyList(),
    val activeRecipes:List<RecipeWithDoctorName> =emptyList(),
    val isLoading: Boolean= false,
    val error:String?=null

)
data class RecipeWithDoctorName(
    val recipe: Recipe,
    val doctorName: String
)