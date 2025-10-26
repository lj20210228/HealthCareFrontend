package com.example.mobilehealthcare

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.compose.MobileHealthCareTheme
import com.example.mobilehealthcare.service.TokenStorage
import com.example.mobilehealthcare.ui.navhost.DoctorNavHost
import com.example.mobilehealthcare.ui.navhost.PatientNavHost
import com.example.mobilehealthcare.ui.screens.login.LoginScreen
import com.example.mobilehealthcare.ui.screens.register.RegisterScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var tokenStorage: TokenStorage

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MobileHealthCareTheme {
                val token = tokenStorage.getToken()
                val role = tokenStorage.getRole()
                Log.d("Token",token.toString())
                Log.d("Role",role.toString())
                val navController = rememberNavController()
                when{
                    token==null->{
                        AuthNavHost(navController)

                    }
                    role=="ROLE_PATIENT"->{
                        PatientNavHost(navController)
                    }
                    role=="ROLE_DOCTOR"->{
                        DoctorNavHost(navController)
                    }
                    else->{
                        AuthNavHost(navController)
                    }
                }




            }
        }
    }
}



@Composable
fun AuthNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController = navController) }
        composable("register") { RegisterScreen(modifier = Modifier, navController = navController) }
    }
}