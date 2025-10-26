package com.example.mobilehealthcare.ui.screens.login

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mobilehealthcare.R
import com.example.mobilehealthcare.models.request.LoginRequest
import com.example.mobilehealthcare.ui.screens.AuthState
import com.example.mobilehealthcare.ui.screens.AuthViewModel


@Composable
fun LoginScreen(
    viewModel: AuthViewModel= hiltViewModel(),
    navController: NavController
){
    var passwordVisible by remember { mutableStateOf(false) }

    val context= LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val uiState=viewModel.uiState.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondary)
            .padding(vertical = 64.dp, horizontal = 32.dp)
        , horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {



        Text(
            style = MaterialTheme.typography.titleLarge,
            text = "Uloguj se",
            modifier= Modifier.padding(vertical = 16.dp)
        )


        Text(
            "Email",
            style = MaterialTheme.typography.titleMedium,

            )
        OutlinedTextField(
            value = email,
            shape = RoundedCornerShape(16.dp),
            modifier= Modifier
                .fillMaxWidth()
                ,
            onValueChange = { email=it },
            placeholder = {
                Text("Unesite email")
            }

        )

        Spacer(Modifier.size(16.dp))

        Text(
            "Lozinka",
            style = MaterialTheme.typography.titleMedium,

            )
        OutlinedTextField(
            value = password,
            onValueChange = { password=it },
            shape = RoundedCornerShape(16.dp),

            modifier= Modifier
                .fillMaxWidth()
               ,
            placeholder = { Text("Unesite lozinku") },
            label = { Text("Lozinka") },
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible)
                    painterResource(id = R.drawable.outline_lock_24)
                else
                    painterResource(id = R.drawable.outline_lock_open_24)

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(painter = image, contentDescription = if (passwordVisible) "Sakrij lozinku" else "Prikaži lozinku")
                }
            },
        )
        Spacer(Modifier.size(24.dp))
        Button(
            modifier= Modifier.padding(vertical = 32.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(shape = RoundedCornerShape(16.dp), border = BorderStroke(1.dp,Color.White))
                .background(MaterialTheme.colorScheme.secondary)
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            onClick = {
                viewModel.login(LoginRequest(email,password))
            },
            enabled = email.isNotEmpty()&&password.isNotEmpty(),
            colors = ButtonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer, disabledContainerColor = MaterialTheme .colorScheme.onPrimaryContainer,
                contentColor =Color.White,
                disabledContentColor = Color.White
            ),
        ) {
            Text(
                "Uloguj se"
            )

        }
        Spacer(Modifier.size(16.dp))

        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically

        ){
            Text(
                text = "Nemate nalog?",
                style = MaterialTheme.typography.bodyMedium,
                )

            TextButton(
                onClick = { navController.navigate("register") }
            ) {
                Text(
                    text = "Registrujte se",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

        }


    }
    when (val state = uiState.value) {
        is AuthState.Success -> {
            LaunchedEffect(Unit) {
                Toast.makeText(context, "Uspešna prijava", Toast.LENGTH_SHORT).show()
                navController.navigate("home") {
                    popUpTo("home") { inclusive = true }
                }
            }
        }

        is AuthState.Error -> {
            Toast.makeText(context, state.message ?: "Greška pri prijavi", Toast.LENGTH_SHORT)
                .show()
        }

        AuthState.Nothing -> Unit
    }
}
