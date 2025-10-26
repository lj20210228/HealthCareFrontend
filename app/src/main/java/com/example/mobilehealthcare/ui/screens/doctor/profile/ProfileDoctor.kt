package com.example.mobilehealthcare.ui.screens.doctor.profile

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
fun ProfileDoctor(navController: NavController) {
    val user= User(
        email = "user@gmail.com",
        password ="dladpqnqnx",
        role = Role.ROLE_DOCTOR,
    )
    val doctor= Doctor(

        fullName = "Jovana Markovic",
        specialization = "Neurolog",
        maxPatients = 40,
        currentPatients = 12,
        isGeneral = false
    )
    val hospital= Hospital(
        name = "Klinicki centar Srbije",
        city = "Beograd",
        address = "Pasterova 3",
        id = "1"
    )
    val workTime1= WorkTime(
        startTime = LocalTime.of(8, 0),
        endTime = LocalTime.of(15, 0),
        doctorId = "1",
        dayInWeek = DayInWeek.MONDAY,
        id = "1"
    )
    val workTime2= WorkTime(
        startTime = LocalTime.of(8, 0),
        endTime = LocalTime.of(15, 0),
        doctorId = "1",
        dayInWeek = DayInWeek.TUESDAY,
        id = "1"
    )
    val workTime3= WorkTime(
        startTime = LocalTime.of(8, 0),
        endTime = LocalTime.of(15, 0),
        doctorId = "1",
        dayInWeek = DayInWeek.WEDNESDAY,
        id = "1"
    )
    val workTime4= WorkTime(
        startTime = LocalTime.of(8, 0),
        endTime = LocalTime.of(15, 0),
        doctorId = "1",
        dayInWeek = DayInWeek.THURSDAY,
        id = "1"
    )
    val workTime5= WorkTime(
        startTime = LocalTime.of(8, 0),
        endTime = LocalTime.of(15, 0),
        doctorId = "1",
        dayInWeek = DayInWeek.FRIDAY,
        id = "1"
    )
    val workTime6= WorkTime(
        startTime = LocalTime.of(8, 0),
        endTime = LocalTime.of(15, 0),
        doctorId = "1",
        dayInWeek = DayInWeek.SATURDAY,
        id = "1"
    )
    val workTime7= WorkTime(
        startTime = LocalTime.of(8, 0),
        endTime = LocalTime.of(15, 0),
        doctorId = "1",
        dayInWeek = DayInWeek.SUNDAY,
        id = "1"
    )
    val list=listOf(workTime1,workTime2,workTime3,workTime4,workTime5,workTime6,workTime7)
    Column(
        modifier = Modifier.fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 16.dp),

    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            val initials = doctor.fullName
                .trim()
                .split(" ")
                .filter { it.isNotBlank() }
                .take(2)
                .joinToString("") { it.first().uppercase() }


                Text(
                    text = initials,
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(
                            color = MaterialTheme.colorScheme.primary
                        )
                        .padding(24.dp)
                )
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = doctor.fullName,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = doctor.specialization,
                    style = MaterialTheme.typography.labelMedium
                )
            }
            Button(
                onClick = {},
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.tertiaryContainer),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                ),
                modifier = Modifier.height(32.dp)


            ) {
                Text(
                    text = "Izmeni",
                    style = MaterialTheme.typography.bodySmall
                )
            }


        }
        Divider(modifier = Modifier.padding(vertical = 46.dp), thickness = 2.dp, color = androidx.compose.ui.graphics.Color.LightGray)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 12.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.mail),
                contentDescription = null,
                modifier = Modifier.padding(end=8.dp)
                    .size(16.dp)
            )
            Column {
                Text(
                    text="Email",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodyMedium


                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 12.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.hospital),
                contentDescription = null,
                modifier = Modifier.padding(end=8.dp)
                    .size(16.dp)
            )
            Column {
                Text(
                    text="Bolnica",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = hospital.name+", "+hospital.city,
                    style = MaterialTheme.typography.bodyMedium


                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 12.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.stethoscope),
                contentDescription = null,
                modifier = Modifier.padding(end=8.dp)
                    .size(16.dp)
            )
            Column {
                Text(
                    text="Specijalizacija",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = doctor.specialization,
                    style = MaterialTheme.typography.bodyMedium


                )
            }
        }
        Spacer(Modifier.size(32.dp))

        StatisticCart(
            patients = doctor.currentPatients,
            termins = 32
        )

        Spacer(Modifier.size(32.dp))
        WorkTimeCart(
            doctor=doctor,
            list
        )
        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {

            },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text(
                "Izloguj se"
            )
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