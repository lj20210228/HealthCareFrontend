package com.example.mobilehealthcare.ui.screens

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilehealthcare.domain.Hospital
import com.example.mobilehealthcare.models.request.LoginRequest
import com.example.mobilehealthcare.models.request.RegisterRequest
import com.example.mobilehealthcare.models.response.BaseResponse
import com.example.mobilehealthcare.models.response.ListResponse
import com.example.mobilehealthcare.models.response.RegisterResponse
import com.example.mobilehealthcare.service.AuthService
import com.example.mobilehealthcare.service.HospitalService
import com.example.mobilehealthcare.service.TokenStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.Response
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(val authService: AuthService,val
                                        hospitalService: HospitalService,val tokenStorage: TokenStorage): ViewModel() {
    private val _uiState= MutableStateFlow<AuthState>(AuthState.Nothing)
    val uiState=_uiState.asStateFlow()


    private val _hospitalsState = MutableStateFlow(HospitalsUiState())
    val hospitalsState = _hospitalsState.asStateFlow()
     fun register(request: RegisterRequest){
        viewModelScope.launch {
            Log.d("Reguest",request.toString())
            val response=authService.register(request)
            Log.d("Response",response.body().toString())
            val body = response.body()
            when (body!!) {
                is BaseResponse.SuccessResponse -> {
                    _uiState.value = AuthState.Success(data = body.data!!, message = body.message)
                    tokenStorage.saveToken(body.data.token)
                }
                is BaseResponse.ErrorResponse -> {
                    _uiState.value = AuthState.Error(message = body.message ?: body.exception ?: "Nepoznata greška")
                }
            }

        }

    }
    fun login(request: LoginRequest){
        viewModelScope.launch {
            Log.d("Reguest",request.toString())

            val response=authService.login(request)
            Log.d("Response",response.body().toString())

            val body = response.body()
            if (body != null) {
                when (body) {
                    is BaseResponse.SuccessResponse -> {
                        val data = body.data
                        if (data != null) {
                            _uiState.value = AuthState.Success(data = data, message = body.message)
                            tokenStorage.saveToken(data.token)
                        } else {
                            _uiState.value = AuthState.Error("Data je null")
                        }
                    }
                    is BaseResponse.ErrorResponse -> {
                        _uiState.value = AuthState.Error(body.message ?: "Nepoznata greška")
                    }
                }
            } else {
                _uiState.value = AuthState.Error("Body je null")
            }

        }
    }
    fun getAllHospitals(){
        viewModelScope.launch {
            try {
                val response = hospitalService.getAllHospitals()
                val body = response.body()

                when (body) {
                    is ListResponse.SuccessResponse -> {
                        _hospitalsState.value = HospitalsUiState(
                            hospitals = (body.data ?:emptyList()) as List<Hospital>,
                            isLoading = false
                        )
                    }
                    is ListResponse.ErrorResponse -> {
                        _hospitalsState.value = HospitalsUiState(
                            error = body.message ?: "Greška pri učitavanju bolnica",
                            isLoading = false
                        )
                    }
                    else -> {
                        _hospitalsState.value = HospitalsUiState(
                            error = "Nepoznat odgovor sa servera",
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _hospitalsState.value = HospitalsUiState(
                    error = "Greška: ${e.localizedMessage}",
                    isLoading = false
                )
            }
        }
    }


}
sealed class AuthState{
    object Nothing: AuthState()
    data class Error(val message: String?=null): AuthState()
    data class Success(val data: RegisterResponse, val message: String?=null): AuthState()
}
data class HospitalsUiState(
    val isLoading: Boolean = false,
    val hospitals: List<Hospital> = emptyList(),
    val error: String? = null
)