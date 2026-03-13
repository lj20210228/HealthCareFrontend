package com.example.mobilehealthcare

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context.NOTIFICATION_SERVICE
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
import androidx.core.content.ContextCompat.getSystemService
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
import android.content.Context


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var tokenStorage: TokenStorage
    @Inject
    lateinit var  authStatusViewModel: AuthStatusViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        createNotificationChannel()

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
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val name = "Chat Notifications"
        val descriptionText = "Notifikacije za nove poruke"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel("chat_channel", name, importance).apply {
            description = descriptionText
        }

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

}



@Composable
fun AuthNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController = navController) }
        composable("register") { RegisterScreen(modifier = Modifier, navController = navController) }
    }
}