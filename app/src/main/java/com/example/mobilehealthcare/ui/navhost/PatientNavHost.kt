package com.example.mobilehealthcare.ui.navhost

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.mobilehealthcare.ui.screens.Screen
import com.example.mobilehealthcare.ui.screens.patient.doctors.DoctorScreenForPatients
import com.example.mobilehealthcare.ui.screens.patient.home.HomePatientScreen
import com.example.mobilehealthcare.ui.screens.patient.recipes.PatientRecipesScreen
import com.example.mobilehealthcare.ui.screens.shared.chat.ChatScreen
import com.example.mobilehealthcare.ui.screens.shared.message.MessageScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PatientNavHost(
    navController: NavHostController,

    ){
    Scaffold (
        bottomBar ={PatientBottomBar(navController)}
    ){innerPadding->
        NavHost(
            navController=navController,
            startDestination= Screen.PatientScreen.Home.route
            ,modifier= Modifier.padding(innerPadding)
        ){
            composable(Screen.PatientScreen.Home.route) {
                HomePatientScreen(navController=navController)

            }
            composable(Screen.PatientScreen.Chats.route) {
                ChatScreen(navController = navController)

            }
            composable(Screen.PatientScreen.Therapy.route) {
                PatientRecipesScreen()


            }
            composable(Screen.PatientScreen.DoctorsAndTermins.route){

                DoctorScreenForPatients()

            }
            composable(
                route = Screen.PatientScreen.Message.route,
                arguments = listOf(
                    navArgument("chatId") { type = NavType.StringType },
                    navArgument("receiverId") { type = NavType.StringType }
                )
            ) { backStackEntry ->

                    val chatId = backStackEntry.arguments?.getString("chatId")
                    val receiverId = backStackEntry.arguments?.getString("receiverId")
                    MessageScreen(chatId = chatId, receiverName = receiverId)


            }

        }

    }
}
@Composable
fun PatientBottomBar(navController: NavHostController){
    val items=listOf(
        Screen.PatientScreen.Home,

        Screen.PatientScreen.Chats,
        Screen.PatientScreen.Therapy,
        Screen.PatientScreen.DoctorsAndTermins

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