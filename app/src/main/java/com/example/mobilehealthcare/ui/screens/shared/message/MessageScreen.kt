package com.example.mobilehealthcare.ui.screens.shared.message


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mobilehealthcare.domain.Message
import com.example.mobilehealthcare.ui.screens.shared.message.MessageViewModel
import java.time.format.DateTimeFormatter

@Composable
fun MessageScreen(
    chatId: String?,
    receiverName: String?,
    viewModel: MessageViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.connect()
    }
    DisposableEffect(Unit) {
        onDispose { viewModel.shutdown() }
    }

    if (chatId == null) {
        Text("Nepoznat chat")
        return
    }

    val uiState by viewModel.uiState.collectAsState()
    var textState by remember { mutableStateOf(TextFieldValue("")) }

    val listState = rememberLazyListState()

    LaunchedEffect(uiState.messages.size) {
        if (uiState.messages.isNotEmpty()) {
            listState.animateScrollToItem(uiState.messages.lastIndex)
        }
    }

    Column(modifier = Modifier.fillMaxSize()
        .background(brush = Brush.radialGradient(
        colors = listOf(
            Color(0xFF81D4FA), // svetlo plava
            Color(0xFF0288D1),
        )


        ,

        center = Offset.Unspecified,
        radius = 1000f,

    ))) {


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0288D1))
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = receiverName ?: "Chat",
                style = MaterialTheme.typography.headlineSmall.copy(color = Color.White)
            )
        }

        LazyColumn(
            state = listState,
            modifier = Modifier

                .weight(1f)
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),


        ) {

            items(uiState.messages) { message ->
                MessageItem(message, viewModel.userId!!)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = textState,
                onValueChange = { textState = it },
                placeholder = { Text("Unesite poruku") },
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .weight(1f)
,
                maxLines = 4,
                shape =RoundedCornerShape(16.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    if (textState.text.isNotBlank()) {
                        viewModel.sendMessage(textState.text)
                        textState = TextFieldValue("")
                    }
                },
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0288D1))
            ) {
                Text("➤", color = Color.White, textAlign = TextAlign.Center)
            }
        }
    }
}

@Composable
fun MessageItem(message: Message, currentUserId: String) {
    val isSentByUser = message.senderId == currentUserId
    val time = message.timeStamp?.format(DateTimeFormatter.ofPattern("HH:mm")) ?: ""

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = if (isSentByUser) Arrangement.End else Arrangement.Start
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .background(
                    color = if (isSentByUser) Color(0xFF0288D1) else Color(0xFFE0E0E0),
                    shape = RoundedCornerShape(
                        topStart = 12.dp,
                        topEnd = 12.dp,
                        bottomStart = if (isSentByUser) 12.dp else 0.dp,
                        bottomEnd = if (isSentByUser) 0.dp else 12.dp
                    )
                )
                .padding(12.dp)
        ) {
            Text(
                text = message.content,
                color = if (isSentByUser) Color.White else Color.Black,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = time,
                modifier = Modifier.align(Alignment.End),
                color = if (isSentByUser) Color.White.copy(alpha = 0.7f) else Color.Gray,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}
