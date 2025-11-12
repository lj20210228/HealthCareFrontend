package com.example.mobilehealthcare

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.mobilehealthcare.ui.screens.shared.AuthStatusViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var tokenStorage: TokenStorage
    @Inject
    lateinit var  authStatusViewModel: AuthStatusViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MobileHealthCareTheme {
                val isLoggedIn by authStatusViewModel.isLoggedIn.collectAsState()
                val role by authStatusViewModel.userRole.collectAsState()
                val currentRole = role
                Log.d("Role",role.toString())
                val navController = rememberNavController()

                when{
                    !isLoggedIn->{
                        AuthNavHost(navController)

                    }
                    currentRole=="ROLE_PATIENT"->{
                        PatientNavHost(navController)
                    }
                    currentRole=="ROLE_DOCTOR"->{
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