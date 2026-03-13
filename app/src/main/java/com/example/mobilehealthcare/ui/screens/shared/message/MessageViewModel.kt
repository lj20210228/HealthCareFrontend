package com.example.mobilehealthcare.ui.screens.shared.message

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilehealthcare.domain.Chat
import com.example.mobilehealthcare.domain.Message
import com.example.mobilehealthcare.models.response.BaseResponse
import com.example.mobilehealthcare.models.response.ListResponse
import com.example.mobilehealthcare.service.ChatService
import com.example.mobilehealthcare.service.MessageService
import com.example.mobilehealthcare.service.TokenStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val messageService: MessageService,
    private val tokenStorage: TokenStorage,
    val chatService: ChatService,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val chatId: String = savedStateHandle["chatId"] ?: ""
    private val _uiState= MutableStateFlow<MessageUiState>(MessageUiState())
    val uiState=_uiState.asStateFlow()
    val userId = tokenStorage.getUserId()

    private val client: OkHttpClient= OkHttpClient()
    var webSocket: WebSocket?=null



    init {
        loadChat()
        loadMessages(chatId)
    }

    fun loadMessages(chatId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true)}


            val response = messageService.getAllMessagesInChat(chatId)
            Log.d("MessageResponse",response.toString())

            val body = response.body()
            Log.d("MessageResponseBody",body.toString())
            if (body is ListResponse.SuccessResponse) {
                _uiState.update { it.copy(isLoading = false, messages = body.data as List<Message>) }
            }
        }
    }
    fun loadChat(){
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true)}

            val response=chatService.getChatForId(chatId)
            Log.d("ChatInMessage",response.toString())

            val body=response.body()
            Log.d("ChatInMessageBody",body.toString())

            if (body is BaseResponse.SuccessResponse){
                _uiState.update { it.copy(isLoading = false, chat = body.data)}
            }

        }
    }

    fun sendMessage( content: String) {
        viewModelScope.launch {
            val chat = _uiState.value.chat ?: return@launch
            val recipientId =
                if (tokenStorage.getDoctorId() != null)
                    chat.patientId
                else
                    chat.doctorId
            val message = Message(
                chatId = chatId,
                content = content,
                senderId = userId!!,
                recipientId =recipientId!!
            )
            val socket=webSocket?.send(Json.encodeToString(Message.serializer(),message))
            Log.d("Socket",socket.toString())



            val response = messageService.sentMessage(message)
            if (response.isSuccessful) {
                loadMessages(chatId)
            }
        }
    }
    fun shutdown(){
        webSocket?.close(1000,"Closed Manually")
        client.dispatcher.executorService.shutdown()
    }
    fun connect(){
        webSocket=client.newWebSocket(createRequest(),object : WebSocketListener(){
            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                val message= Json.decodeFromString<Message>(text)
                println("new message received:$message")
                _uiState.update { it.copy(messages = it.messages+message) }
            }
        })
    }
    fun createRequest(): Request {
        return Request
            .Builder()
            .url("http://10.150.228.21:8080/chat")
            .build()
    }




}
data class MessageUiState(
    val chat: Chat?=null,
    val messages: List<Message> = emptyList(),
    val isLoading: Boolean=false,
    val error: String?=null
)
