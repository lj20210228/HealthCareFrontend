package com.example.mobilehealthcare

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mobilehealthcare.service.TokenStorage
import com.example.mobilehealthcare.ui.screens.Screen
import com.example.mobilehealthcare.ui.screens.login.LoginScreen
import com.example.mobilehealthcare.ui.screens.register.RegisterScreen
import com.example.mobilehealthcare.ui.theme.MobileHealthCareTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var tokenStorage: TokenStorage

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MobileHealthCareTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { it->
                    Surface (
                        modifier = Modifier.padding(it)
                    ){
                        tokenStorage.clearToken()
                        val token=tokenStorage.getToken()
                        val navController = rememberNavController()
                        NavHost(navController = navController, startDestination = if (token==null)
                            Screen.Login.route else Screen.Home.route) {
                            composable(Screen.Login.route) {
                                LoginScreen(
                                    navController=navController
                                )
                            }
                            composable(Screen.Register.route) {
                                RegisterScreen(
                                    modifier = Modifier,
                                    navController=navController
                                )
                            }

                            composable(Screen.Home.route) {

                                Box(modifier = Modifier.fillMaxSize()){
                                    Text(
                                        text = "Hello"
                                    )
                                }
                            }

                        }
                    }

                }
            }
        }
    }
}



