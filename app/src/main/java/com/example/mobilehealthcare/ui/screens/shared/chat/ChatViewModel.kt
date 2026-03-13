package com.example.mobilehealthcare.ui.screens.shared.chat

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
import com.example.mobilehealthcare.service.SelectedDoctorService
import com.example.mobilehealthcare.service.TokenStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.internal.notify
import javax.inject.Inject
@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatService: ChatService,
    private val doctorService: DoctorService,
    private val patientService: PatientService,
    private val tokenStorage: TokenStorage,
    private val selectedDoctorService: SelectedDoctorService
): ViewModel() {

     val doctorId = tokenStorage.getDoctorId()
     val patientId = tokenStorage.getPatientId()
    val userId=tokenStorage.getUserId()

    private var _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        if (patientId != null) {
            getDoctorsForPatient(patientId)
            getChatsForPatient()
        } else if (doctorId != null) {
            getPatientsForDoctor(doctorId)
            getChatsForDoctor()
        }
    }

    fun getChatsForPatient() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        val response = chatService.getAllChatsForPatient(userId!!)
        Log.d("PatientsChats",response.body().toString())

        val body = response.body()
        if (body is ListResponse.SuccessResponse) {
            val chatsForUi = body.data?.mapNotNull { chat ->
                val doctor = (doctorService.getDoctorForUserId(chat?.doctorId!!).body() as? BaseResponse.SuccessResponse)?.data
                ChatForUi(chat = chat!!, doctor = doctor)
            } ?: emptyList()
            _uiState.update { it.copy(isLoading = false, chats = chatsForUi) }
        } else {
            _uiState.update { it.copy(isLoading = false, errorMessage = response.message()) }
        }
    }

    fun getChatsForDoctor() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        val response = chatService.getAllChatsForDoctor(userId!!)
        Log.d("DoctorChats",response.toString())


        val body = response.body()
        if (body is ListResponse.SuccessResponse) {
            val chatsForUi = body.data?.mapNotNull { chat ->
                val patient = (patientService.getPatientByUserId(chat?.patientId!!).body() as? BaseResponse.SuccessResponse)?.data
                ChatForUi(chat = chat!!, patient = patient)
            } ?: emptyList()
            _uiState.update { it.copy(isLoading = false, chats = chatsForUi) }
        } else {
            _uiState.update { it.copy(isLoading = false, errorMessage = response.message()) }
        }
    }

    fun createChatWithReceiver(receiverId: String, isDoctor: Boolean, onSuccess: (chatId: String) -> Unit) = viewModelScope.launch {
        val userId = userId
        val chat = Chat(
            doctorId = if (isDoctor) userId!! else receiverId,
            patientId = if (isDoctor) receiverId else userId!!,
        )
        val response = chatService.addChat(chat)
        val body = response.body()
        Log.d("ChatViewModel", "Creating chat with $receiverId, isDoctor=$isDoctor")

        if (body is BaseResponse.SuccessResponse) {
            onSuccess(body.data!!.id!!)
        } else {
            _uiState.update { it.copy(errorMessage = response.message()) }
        }
    }

    fun getPatientsForDoctor(doctorId: String) = viewModelScope.launch {
        val response = selectedDoctorService.getPatientsForSelectedDoctor(doctorId)
        Log.d("Patients",response.body().toString())
        val body = response.body()
        if (body is ListResponse.SuccessResponse) {
            _uiState.update { it.copy(patients = body.data as List<Patient>) }
        }
    }

    fun getDoctorsForPatient(patientId: String) = viewModelScope.launch {
        val response = selectedDoctorService.getAllSelectedDoctorsForPatint(patientId)
        Log.d("Doctors",response.body().toString())

        val body = response.body()
        if (body is ListResponse.SuccessResponse) {
            _uiState.update { it.copy(doctors = body.data as List<Doctor>) }
        }
    }
}

data class UiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val chats: List<ChatForUi> = emptyList(),
    val patients: List<Patient> = emptyList(),
    val doctors: List<Doctor> = emptyList()
)

data class ChatForUi(
    val chat: Chat,
    val patient: Patient? = null,
    val doctor: Doctor? = null
)
