package com.example.mobilehealthcare.ui.navhost

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.mobilehealthcare.ui.screens.Screen
import com.example.mobilehealthcare.ui.screens.doctor.home.DoctorHome

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DoctorNavHost(
    navController: NavHostController,

){
    Scaffold (
        bottomBar ={DoctorBottomBar(navController)}
    ){innerPadding->
        NavHost(
            navController=navController,
            startDestination= Screen.DoctorScreen.Home.route
            ,modifier= Modifier.padding(innerPadding)
        ){
           composable(Screen.DoctorScreen.Home.route) {
               DoctorHome()

           }
            composable(Screen.DoctorScreen.Messages.route) {
                Box(){
                    Text(
                        "Messages"
                    )
                }

            }
            composable(Screen.DoctorScreen.Profile.route) {
                Box(

                ){
                    Text(
                        "Profile"
                    )
                }


            }
            composable(Screen.DoctorScreen.Patients.route) {
                Box(){
                    Text("Patients")
                }

            }
        }

    }
}
@Composable
fun DoctorBottomBar(navController: NavHostController){
    val items=listOf(
        Screen.DoctorScreen.Home,
        Screen.DoctorScreen.Patients,
        Screen.DoctorScreen.Messages,
        Screen.DoctorScreen.Profile
    )
    NavigationBar {
        val currentRoute=navController.currentBackStackEntryAsState().value?.destination?.route
        items.forEach {
            NavigationBarItem(
                icon = { Icon(painter = painterResource(it.icon), contentDescription = null,
                    modifier = Modifier.size(24.dp)) },
                label = { Text(it.title) },
                selected = currentRoute==it.route,
                onClick = {
                    navController.navigate(it.route){
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop=true
                    }
                }
            )
        }

    }
}