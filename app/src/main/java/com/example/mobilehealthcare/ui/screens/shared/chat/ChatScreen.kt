package com.example.mobilehealthcare.ui.screens.shared.chat

import android.util.Log
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.mobilehealthcare.R
import com.example.mobilehealthcare.domain.Doctor
import com.example.mobilehealthcare.domain.Patient
import com.example.mobilehealthcare.ui.screens.Screen
import com.example.mobilehealthcare.ui.screens.patient.doctors.isDoctorWorkingOn

@Composable
fun ChatScreen(
    navController: NavHostController,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = Color(0xFF0288D1),
                contentColor = Color.White
            ) {
                Icon(painterResource(R.drawable.outline_add_24), contentDescription = "Novi čet")
            }
        }
    ) { padding ->
        Box(modifier = Modifier
            .background(brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFF81D4FA), // svetlo plava
                    Color(0xFF0288D1),
                ),
                center = Offset.Unspecified,
                radius = 1000f
            ))
            .fillMaxSize()
            .padding(padding)) {
            when {
                uiState.isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                uiState.errorMessage != null -> Text(
                    text = uiState.errorMessage!!,
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.Red
                )
                else -> {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier .padding( 16.dp)) {
                        item {
                            Text(
                                text = "Vaše poruke",
                                fontSize = 32.sp
                            )
                        }
                        items(uiState.chats) { chat ->
                            Log.d("ChatScreen",chat.toString())
                            val name = chat.patient?.fullName ?: chat.doctor?.fullName ?: "Nepoznato"
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        navController.navigate("patientsChat/${chat.chat.id}/$name")
                                    }
                            ) {
                                Row(modifier = Modifier.padding(16.dp)) {
                                    Box(
                                        modifier = Modifier
                                            .size(45.dp)
                                            .background(Color(0xFF0288D1), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(name.first().uppercase(), color = Color.White)
                                    }
                                    Text(
                                        text = name,
                                        modifier = Modifier.padding(start = 12.dp),
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Izaberite osobu") },
                    text = {
                        val people: List<Any> = if (viewModel.doctorId != null) uiState.patients else uiState.doctors

                        Log.d("People",people.toString())
                        LazyColumn {
                            items(people.size) { index ->
                                val person = people[index]
                                val name = when(person) {
                                    is Patient -> person.fullName
                                    is Doctor -> person.fullName

                                    else -> {"Nepoznato"}
                                }
                                Text(
                                    text = name,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            val receiverId = when(person) {
                                                is Patient -> person.userId
                                                is Doctor -> person.userId
                                                else -> return@clickable
                                            } ?: return@clickable

                                            val isDoctor = viewModel.doctorId != null

                                            viewModel.createChatWithReceiver(receiverId, isDoctor)

                                            showDialog = false

                                        }
                                        .padding(12.dp)
                                )

                            }
                        }

                    },
                    confirmButton = {},
                    dismissButton = {
                        TextButton(onClick = { showDialog = false }) {
                            Text("Otkaži")
                        }
                    }
                )
            }
        }
    }
}
