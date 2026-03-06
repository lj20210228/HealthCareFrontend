package com.example.mobilehealthcare.ui.screens.shared.message

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilehealthcare.domain.Chat
import com.example.mobilehealthcare.domain.Doctor
import com.example.mobilehealthcare.domain.Patient
import com.example.mobilehealthcare.models.response.BaseResponse
import com.example.mobilehealthcare.models.response.ListResponse
import com.example.mobilehealthcare.service.ChatService
import com.example.mobilehealthcare.service.DoctorService
import com.example.mobilehealthcare.service.PatientService
import com.example.mobilehealthcare.service.TokenStorage
import com.example.mobilehealthcare.service.UserService
import dagger.hilt.android.lifecycle.HiltViewModel
import hilt_aggregated_deps._com_example_mobilehealthcare_ui_screens_AuthViewModel_HiltModules_BindsModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ChatViewModel @Inject constructor(
    val chatService: ChatService,
    val doctorService: DoctorService,
    val patientService: PatientService,
    val tokenStorage: TokenStorage
): ViewModel() {
    val doctorId=tokenStorage.getDoctorId()
    val patientId=tokenStorage.getPatientId()
    private var _uiState =  MutableStateFlow<UiState>(UiState())
    var uiState=_uiState.asStateFlow()
    init {
        if (patientId!=null)
            getChatsForPatient(patientId)
        if (doctorId!=null)
            getChatsForDoctor(doctorId)
    }

    fun getChatsForPatient(patientId:String){
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
           val response= chatService.getAllChatsForPatient(patientId)
            Log.d("Response",response.toString())

            val body=response.body()
            Log.d("ResponseBody",body.toString())
            if (body is ListResponse.SuccessResponse){
                val chats=body.data
                Log.d("ResponseBodyData",chats.toString())

                val chatsForUi= mutableListOf<ChatForUi> ()
                chats?.mapNotNull {chat ->
                    val doctorResponse=doctorService.getDoctorForId(chat?.doctorId!!).body()
                    var doctor: Doctor?=null
                    if (doctorResponse is BaseResponse.SuccessResponse)
                    {
                        doctor=doctorResponse.data!!
                    }

                    chatsForUi.add(ChatForUi(doctor=doctor, chat = chat))
                }
                _uiState.update { it.copy(isLoading = false,chats=chatsForUi) }

            }
            if (response.body() is ListResponse.ErrorResponse){
                _uiState.update { it.copy(isLoading = false, errorMessage = response.message()) }
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }
    fun getChatsForDoctor(doctorId:String){
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val response= chatService.getAllChatsForDoctor(doctorId)
            val body=response.body()
            Log.d("Response",body.toString())

            if (body is ListResponse.SuccessResponse){
                val chats=body.data
                val chatsForUi= mutableListOf<ChatForUi> ()
                chats?.mapNotNull {chat ->
                    val patientResponse=patientService.getPatient(chat?.patientId!!).body()
                    var patient: Patient?=null
                    if (patientResponse is BaseResponse.SuccessResponse)
                    {
                        patient=patientResponse.data!!
                    }

                    chatsForUi.add(ChatForUi(patient=patient, chat = chat))
                }
                _uiState.update { it.copy(isLoading = false,chats=chatsForUi) }

            }
            if (response.body() is ListResponse.ErrorResponse){
                _uiState.update { it.copy(isLoading = false, errorMessage = response.message()) }
            }
            _uiState.update { it.copy(isLoading = false) }

        }
    }

}
data class UiState(
    var isLoading: Boolean=false,
    var errorMessage: String?=null,
    val chats:List<ChatForUi> =emptyList()
)
data class ChatForUi(
    val chat: Chat,
    val patient: Patient?=null,
    val doctor: Doctor?=null
)