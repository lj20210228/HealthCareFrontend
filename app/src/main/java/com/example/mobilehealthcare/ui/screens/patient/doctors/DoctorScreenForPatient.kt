package com.example.mobilehealthcare.ui.screens.patient.doctors

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.mobilehealthcare.R
import com.example.mobilehealthcare.domain.Doctor
import com.example.mobilehealthcare.domain.Hospital
import com.example.mobilehealthcare.domain.Termin
import com.example.mobilehealthcare.domain.TerminStatus
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorScreenForPatients() {
    var selectedIndex by remember { mutableStateOf(0) }
    val options=listOf("Moji","Preporučeni")
    val doctor1= Doctor(

        fullName = "Jovan Jovanovic",
        specialization = "Kardiolog",
        maxPatients =40,
        currentPatients = 10,
        hospitalId = "1",
        isGeneral =false
    )
    val doctor2= Doctor(

        fullName = "Ivan Petrovic",
        specialization = "Neurolog",
        maxPatients =40,
        currentPatients = 10,
        hospitalId = "1",
        isGeneral =false
    )
    val doctor3= Doctor(

        fullName = "Petar Jovanovic",
        specialization = "Lekar opste prakse",
        maxPatients =40,
        currentPatients = 10,
        hospitalId = "1",
        isGeneral =true
    )
    val doctor4= Doctor(

        fullName = "Ivan Jovanovic",
        specialization = "Dermatolog",
        maxPatients =40,
        currentPatients = 10,
        hospitalId = "1",
        isGeneral =true
    )
    val hospital= Hospital(

        name = "Klinicki Centar",
        city = "Beograd",
        address = "Kneza Milosa 4",
        id="1"
    )
    val termin= Termin(

        date = LocalDate.of(2025,12,11),
        startTime = LocalTime.of(8,0),
        endTime = LocalTime.of(9,0),
        hospitalId = "1",
        status = TerminStatus.SCHEDULDED,
        desctiption = "Pregled"
    )
    val termin2= Termin(

        date = LocalDate.of(2025,12,11),
        startTime = LocalTime.of(9,0),
        endTime = LocalTime.of(10,0),
        hospitalId = "1",
        status = TerminStatus.ON_HOLD,
        desctiption = "Kontrola"
    )
    val termin3= Termin(

        date = LocalDate.of(2025,12,11),
        startTime = LocalTime.of(11,0),
        endTime = LocalTime.of(13,0),
        hospitalId = "1",
        status = TerminStatus.PENDING,
        desctiption = "Snimanje"
    )
    val doctors=listOf(doctor1,doctor2,doctor3,doctor4)
    val termins=listOf(termin,termin2,termin)

    var searchQuery by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    val filteredItems=doctors.filter { it.fullName.contains(searchQuery, ignoreCase = true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFF81D4FA), Color(0xFF0288D1)),
                    center = Offset.Unspecified,
                    radius = 1000f
                )
            )
            .padding(16.dp),
    ){
            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Column {
                    Text(
                        text = "Moji Lekari",
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Text(
                        text = "Upravljajte vašim lekarima",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.Black.copy(0.4f)
                    )
                }
                Image(
                    painter = painterResource(R.drawable.medical_team),
                    modifier = Modifier.size(48.dp),
                    contentDescription = null
                )
            }



        DoctorSearchBar(
            searchQuery = searchQuery,
            onQueryChange = { searchQuery = it
                if(it.isNotEmpty())active=true},
            active = active,
            onActiveChange = { active = it },
            filteredItems = filteredItems
        )
        Spacer(Modifier.size(32.dp))


        LazyColumn {
            items(doctors){
                DoctorCardForPatient(
                    it,
                    hospital,
                    termin
                )
            }
        }


    }

}
@Composable
fun DoctorCardForPatient(
    doctor: Doctor,
    hospital: Hospital,
    termin: Termin
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)

        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(R.drawable.stethoscope),
                    contentDescription = null,
                    modifier = Modifier
                        .size(52.dp)
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = doctor.fullName,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = doctor.specialization,
                        style = MaterialTheme.typography.bodyMedium

                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            modifier = Modifier.size(16.dp),
                            contentDescription = null,
                            painter = painterResource(R.drawable.location)
                        )
                        Text(
                            text="${hospital.name}, ${hospital.city}",
                            style = MaterialTheme.typography.bodyMedium


                        )
                    }

                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 24.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.outline_nest_clock_farsight_analog_24),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(16.dp)
                        ,
                    tint = Color.Blue
                )
                Text(
                    text = "Sledeći slobodan termin je ${termin.date}, ${termin.startTime}",
                    style = MaterialTheme.typography.bodyMedium,
                    color=Color.Blue

                )
            }

            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.calendar),
                        contentDescription = null,
                        modifier = Modifier
                            .size(16.dp)

                    )
                    Text(
                        text="Zakaži termin",
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }


        }



    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorSearchBar(
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    active: Boolean,
    onActiveChange: (Boolean) -> Unit,
    filteredItems: List<Doctor>
) {
    Column {
        SearchBar(
            query = searchQuery,
            onQueryChange = {
                onQueryChange(it)
            },
            onSearch = { onActiveChange(false)},
            active = active,
            onActiveChange = onActiveChange,
            modifier = Modifier
                .padding(start = 12.dp, top = 2.dp, end = 12.dp, bottom = 12.dp)
                .fillMaxWidth(),
            placeholder = { Text("Pretraži lekare") },
        ) {
            if (searchQuery.isNotEmpty()) {
                filteredItems.forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(32.dp)
                            .clickable {
                                onQueryChange(item.fullName)
                                onActiveChange(false)
                            }
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item.fullName,
                            fontSize = 15.sp,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }

}



@Composable
@Preview
fun Preview(){
    DoctorScreenForPatients()
}