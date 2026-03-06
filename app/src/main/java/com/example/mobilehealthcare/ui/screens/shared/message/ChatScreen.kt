package com.example.mobilehealthcare.ui.screens.shared.message

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ChatScreen(
    viewModel: ChatViewModel= hiltViewModel()
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    if (uiState.isLoading==true)
    {
        Column(modifier = Modifier.fillMaxSize(),
            ) {
            CircularProgressIndicator()
        }
    }
    if (uiState.errorMessage!=null){
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = uiState.errorMessage.toString(),
                fontSize = 44.sp
            )

        }
    }
    ChatScreenContent(chats = uiState.chats)

}


@Composable
fun ChatScreenContent(
    chats:List<ChatForUi>
){
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Poruke", style = MaterialTheme.typography.headlineMedium)

        if (chats.isEmpty()){
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Nemate poruka.",
            )
        }else {


            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {

                items(chats) { chat ->
                    Card(
                        modifier = Modifier.fillMaxWidth().clickable { },
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier.size(45.dp)
                                    .background(Color(0xFF0288D1), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                if (chat.patient != null) {
                                    Text(
                                        chat.patient?.fullName?.take(1)?.uppercase()!!,
                                        color = Color.White
                                    )

                                } else {
                                    Text(
                                        chat.doctor?.fullName?.take(1)?.uppercase()!!,
                                        color = Color.White
                                    )

                                }
                            }

                            Column(modifier = Modifier.padding(start = 12.dp)) {
                                if (chat.patient != null) {
                                    Text(
                                        "Pošiljaoc: ${chat.patient.fullName!!}",
                                        fontWeight = FontWeight.Bold
                                    )

                                } else {
                                    Text(
                                        "Pošiljaoc: ${chat.doctor?.fullName!!}",
                                        fontWeight = FontWeight.Bold
                                    )

                                }
                                Text(
                                    "Kliknite za pregled poruka",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}