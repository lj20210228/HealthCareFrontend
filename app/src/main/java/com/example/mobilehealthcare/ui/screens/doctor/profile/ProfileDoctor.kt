package com.example.mobilehealthcare.ui.screens.doctor.profile

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mobilehealthcare.R
import com.example.mobilehealthcare.domain.DayInWeek
import com.example.mobilehealthcare.domain.Doctor
import com.example.mobilehealthcare.domain.Hospital
import com.example.mobilehealthcare.domain.Role
import com.example.mobilehealthcare.domain.User
import com.example.mobilehealthcare.domain.WorkTime
import java.time.LocalTime


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfileDoctor(navController: NavController, viewModel: ProfileDoctorViewModel = hiltViewModel()) {

    var doctor by remember { mutableStateOf<Doctor?>(null) }
    var user by remember { mutableStateOf<User?>(null) }
    var hospital by remember { mutableStateOf<Hospital?>(null) }
    var workTime by remember { mutableStateOf<List<WorkTime>>(emptyList()) }

    val context = LocalContext.current
    val uiState = viewModel.uiState.collectAsState().value
    Log.d("UiState", uiState.toString())

    when (uiState) {
        is ProfileDoctorUiState.Nothing -> { }
        is ProfileDoctorUiState.Error -> {
            Toast.makeText(context, uiState.message ?: "Greška", Toast.LENGTH_SHORT).show()
        }
        is ProfileDoctorUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is ProfileDoctorUiState.Success -> {
            doctor = uiState.doctor
            user = uiState.user
            hospital = uiState.hospital
            workTime = uiState.workTime
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 16.dp),
    ) {

        doctor?.let { d ->
            val initials = d.fullName
                .trim()
                .split(" ")
                .filter { it.isNotBlank() }
                .take(2)
                .joinToString("") { it.first().uppercase() }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = initials,
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(color = MaterialTheme.colorScheme.primary)
                        .padding(24.dp)
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = d.fullName, style = MaterialTheme.typography.titleMedium)
                    Text(text = d.specialization ?: "-", style = MaterialTheme.typography.labelMedium)
                }

                Button(
                    onClick = {},
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.tertiaryContainer),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                    modifier = Modifier.height(32.dp)
                ) {
                    Text(text = "Izmeni", style = MaterialTheme.typography.bodySmall)
                }
            }

            Divider(
                modifier = Modifier.padding(vertical = 46.dp),
                thickness = 2.dp,
                color = androidx.compose.ui.graphics.Color.LightGray
            )
        }

        user?.let { u ->
            InfoRow(icon = R.drawable.mail, title = "Email", value = u.email ?: "-")
        }

        hospital?.let { h ->
            InfoRow(icon = R.drawable.hospital, title = "Bolnica", value = "${h.name ?: "-"}, ${h.city ?: "-"}")
        }

        doctor?.let { d ->
            InfoRow(icon = R.drawable.stethoscope, title = "Specijalizacija", value = d.specialization ?: "-")
        }

        Spacer(Modifier.size(32.dp))

        doctor?.let { d ->
            StatisticCart(patients = d.currentPatients ?: 0, termins = 32)
        }

        Spacer(Modifier.size(32.dp))

        doctor?.let { d ->
            WorkTimeCart(doctor = d, workTimeList = workTime)
        }

        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = { viewModel.logout()
                (context as? Activity)?.recreate()

            },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text("Izloguj se")
        }
    }
}
@Composable
fun InfoRow(icon: Int, title: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 12.dp)) {
        Icon(painter = painterResource(icon), contentDescription = null, modifier = Modifier.padding(end = 8.dp).size(16.dp))
        Column {
            Text(text = title, style = MaterialTheme.typography.bodyLarge)
            Text(text = value, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
@Composable
fun StatisticCart(
    termins:Int=0,
    patients:Int
){
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.secondary)
    ) {
        Text(
            text = "Statistika",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Termini ovog meseca",
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text =termins.toString(),
                style = MaterialTheme.typography.titleSmall
            )
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Broj pacijenata",
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text =patients.toString(),
                style = MaterialTheme.typography.titleSmall
            )
        }
        Spacer(modifier = Modifier.size(8.dp))


    }

}

@Composable
fun WorkTimeCart(
    doctor: Doctor,
    workTimeList:List< WorkTime>
){
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.secondary)
    ){
        Text(
            text = "Radno vreme",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
        )
        workTimeList.forEach {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
             Text(
                 text = when(it.dayInWeek){
                     DayInWeek.MONDAY -> "Ponedeljak"
                     DayInWeek.TUESDAY -> "Utorak"
                     DayInWeek.WEDNESDAY -> "Sreda"
                     DayInWeek.THURSDAY -> "Četvrtak"
                     DayInWeek.FRIDAY -> "Petak"
                     DayInWeek.SATURDAY -> "Subota"
                     DayInWeek.SUNDAY -> "Nedelja"
                 },
                 style = MaterialTheme.typography.bodyMedium
             )
             Text(
                 text =if (it.startTime==null||it.endTime==null)"Neradan dan" else  it.startTime.toString()+"-"+it.endTime.toString(),
                 style = MaterialTheme.typography.labelMedium
             )
            }
        }
        OutlinedButton(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(horizontal = 8.dp, vertical = 8.dp),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.inversePrimary),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.onPrimaryFixedVariant
            )
        ) {
            Text("Izmeni", style = MaterialTheme.typography.labelSmall)
        }
        Spacer(modifier = Modifier.size(8.dp))


    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
fun ProfileDoctorPreview(){
    ProfileDoctor(navController = rememberNavController())
}