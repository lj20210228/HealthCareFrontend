package com.example.mobilehealthcare.ui.screens

import android.graphics.drawable.Icon
import com.example.mobilehealthcare.R
import java.util.function.IntConsumer

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    sealed class PatientScreen(route: String,val title:String,val icon: Int): Screen(route){
        object Home : PatientScreen("homePatient","Početna",R.drawable.home)
        object Messages: PatientScreen("messagesPatient","Poruke",R.drawable.chat)
        object Therapy: PatientScreen("therapy","Terapija",R.drawable.gene_therapy)
        object DoctorsAndTermins: PatientScreen("doctorsAndTermins","Lekari",R.drawable.medical_team)
    }
    sealed class DoctorScreen(route: String,val title:String,val icon: Int): Screen(route){
        object Home: DoctorScreen("homeDoctor","Početna",R.drawable.home)
        object Patients: DoctorScreen("patientsForDoctor","Pacijenti",R.drawable.patient)
        object Messages: DoctorScreen("messagesDoctor","Poruke",R.drawable.chat)
        object Profile: DoctorScreen("profileDoctor","Profil",R.drawable.profle)
    }


}