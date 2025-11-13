package com.example.mobilehealthcare.ui.screens.patient.doctors

import android.app.Activity
import android.app.DatePickerDialog
import android.util.Log
import android.widget.DatePicker
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonColors
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDialog
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.mobilehealthcare.R
import com.example.mobilehealthcare.domain.DayInWeek
import com.example.mobilehealthcare.domain.Doctor
import com.example.mobilehealthcare.domain.Hospital
import com.example.mobilehealthcare.domain.SelectedDoctor
import com.example.mobilehealthcare.domain.Termin
import com.example.mobilehealthcare.domain.TerminStatus
import com.example.mobilehealthcare.domain.WorkTime
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.util.UUID
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Composable
fun DoctorScreenForPatients(
    viewModel: DoctorScreenForPatientViewModel= hiltViewModel()
){
    val uiState by viewModel.uiState.collectAsState()
    when{
        uiState.isLoading->{
            Box(modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center){
                CircularProgressIndicator()
            }
        }
        uiState.error!=null->{
            Box(modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center){
                Text(
                    text = uiState.error?:"Greska",
                    style = MaterialTheme.typography.displaySmall,
                    color = Color.Red
                )
            }
        }
        else->{
            DoctorScreenForPatientsContent(
                myDoctors = uiState.myDoctors,
                allDoctors = uiState.allDoctors,
                hospital =uiState.hospital!!,
                {viewModel.logout()},
                viewModel

            )
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorScreenForPatientsContent(
    myDoctors:List<Pair<Doctor,List< WorkTime?>>>,
    allDoctors:List<Pair<Doctor, List<WorkTime?>>>,
    hospital: Hospital
    , logout:()-> Unit,
    viewModel: DoctorScreenForPatientViewModel

) {
    var selectedIndex by remember { mutableStateOf(0) }
    val options=listOf("Moji","Svi")

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



       /* DoctorSearchBar(
            searchQuery = searchQuery,
            onQueryChange = { searchQuery = it
                if(it.isNotEmpty())active=true},
            active = active,
            onActiveChange = { active = it },
            filteredItems = filteredItems
        )*/
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ){
            options.forEachIndexed { index, label ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = options.size
                    ),
                    icon = {},
                    onClick = { selectedIndex = index },
                    selected = index == selectedIndex,
                    label = { Text(label) },
                    colors = SegmentedButtonColors(
                        activeContainerColor = Color.White,
                        activeContentColor = Color.Black,
                        activeBorderColor = Color.LightGray,
                        inactiveContainerColor =Color.LightGray.copy(0.7f
                        ),
                        inactiveContentColor = Color.Black,
                        inactiveBorderColor = Color.LightGray,
                        disabledActiveContainerColor = Color.Transparent,
                        disabledActiveContentColor =Color.Transparent,
                        disabledActiveBorderColor = Color.Transparent,
                        disabledInactiveContainerColor = Color.Transparent,
                        disabledInactiveContentColor = Color.Transparent,
                        disabledInactiveBorderColor = Color.Transparent
                    )
                )
            }
        }

        Spacer(Modifier.size(32.dp))

        LazyColumn {
            if (selectedIndex==0){
                if (myDoctors.isEmpty()){
                    item {
                        Text(
                            text ="Nemate izabranih lekara",
                            style = MaterialTheme.typography.headlineMedium
                            )
                    }


                }else{
                    items(myDoctors){
                        DoctorCardForPatient(
                            it.first,
                            hospital,
                            myDoctor = true,
                            workTime = it.second,
                            onTerminClick = {  termin->
                                viewModel.addTermin(termin)
                            },
                            onChooseDoctor = {},
                            patientId = viewModel.patientId!!
                        )
                    }
                }

            }else{
                if (allDoctors.isEmpty()){
                    item {
                        Text(
                            text ="Nema lekara u ovoj bolnici",
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }


                }else{
                    items(allDoctors){
                        DoctorCardForPatient(
                            it.first,
                            hospital,
                            workTime = it.second,
                            myDoctor = false,
                            onTerminClick = {},
                            onChooseDoctor =  {doctor->
                                viewModel.addSelectedDoctor(
                                    SelectedDoctor(
                                        doctorId = doctor.id!!,
                                        patientId = viewModel.patientId
                                    )
                                )
                            },
                           patientId =  viewModel.patientId!!
                        )
                    }
                }

            }
            item {
                OutlinedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    onClick = {
                        logout()

                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Izloguj se")
                }
            }

        }



    }

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorCardForPatient(
    doctor: Doctor,
    hospital: Hospital,
    myDoctor: Boolean,
    onTerminClick:(Termin)->Unit,
    onChooseDoctor:(Doctor)-> Unit,
    workTime: List<WorkTime?> =emptyList(),
    patientId: String
){
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )
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
                if (workTime.firstOrNull()!=null){
                    Text(
                        text = "Radno vreme za danas je ${workTime.first()?.startTime}, ${workTime.first()?.endTime}",
                        style = MaterialTheme.typography.bodyMedium,
                        color=Color.Blue

                    )
                }else{
                    Text(
                        text = "Lekar trenutno ne radi",
                        style = MaterialTheme.typography.bodyMedium,
                        color=Color.Blue

                    )
                }

            }

            if (
                myDoctor
            ){
                Button(
                    onClick = {
                        showBottomSheet=true
                    },
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
                            text= "Zakaži termin" ,
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                }
            }else{
                Button(
                    onClick = {
                        onChooseDoctor(doctor)
                    },
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
                            painter = painterResource(R.drawable.stethoscope),
                            contentDescription = null,
                            modifier = Modifier
                                .size(16.dp)

                        )
                        Text(
                            text= "Izaberite lekara kao izabranog" ,
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                }
            }



        }



    }
    if (showBottomSheet){
        PartialBottomSheetForTermins(
          sheetState=sheetState,
            onDissmisRequest = {showBottomSheet=false},
            onAddTermin = onTerminClick,
            workTime=workTime,
            patientId=patientId,
            doctorId = doctor.id!!,
            hospitalId = hospital.id
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun PartialBottomSheetForTermins(
    sheetState: SheetState,
    onDissmisRequest:()-> Unit,
    patientId: String,
    doctorId: String,
    hospitalId: String,
    onAddTermin: (Termin) -> Unit,
    workTime: List<WorkTime?>
) {
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }
    var startTime by remember { mutableStateOf<LocalTime?>(null) }
    var endTime by remember{mutableStateOf<LocalTime?>(null)}

    ModalBottomSheet(
        modifier = Modifier.fillMaxHeight(),
        sheetState = sheetState,
        onDismissRequest = { onDissmisRequest() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = "Zakaži termin",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = { showDatePicker = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = selectedDate?.toString() ?: "Izaberite datum"
                )
            }

            if (showDatePicker) {
                val datePickerState = rememberDatePickerState()
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                selectedDate = java.time.Instant.ofEpochMilli(millis)
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate()
                            }
                            showDatePicker = false
                        }) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDatePicker = false }) {
                            Text("Otkaži")
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }

            Spacer(Modifier.height(24.dp))

            selectedDate?.let { date ->
                val dayOfWeek = when (date.dayOfWeek) {
                    DayOfWeek.MONDAY -> DayInWeek.MONDAY
                    DayOfWeek.TUESDAY -> DayInWeek.TUESDAY
                    DayOfWeek.WEDNESDAY -> DayInWeek.WEDNESDAY
                    DayOfWeek.THURSDAY -> DayInWeek.THURSDAY
                    DayOfWeek.FRIDAY -> DayInWeek.FRIDAY
                    DayOfWeek.SATURDAY -> DayInWeek.SATURDAY
                    DayOfWeek.SUNDAY -> DayInWeek.SUNDAY
                }

                val currentWorkTime = workTime.find { it?.dayIn == dayOfWeek }

                if (currentWorkTime != null) {
                    Text(
                        text = "Radno vreme: ${currentWorkTime.startTime} - ${currentWorkTime.endTime}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Blue
                    )

                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = { showStartTimePicker = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = startTime?.toString() ?: "Izaberite vreme početka")
                    }

                    if (showStartTimePicker) {
                        val timePickerState = rememberTimePickerState()
                        TimePickerDialog(
                            onDismissRequest = { showStartTimePicker = false },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        startTime = LocalTime.of(
                                            timePickerState.hour,
                                            timePickerState.minute
                                        )
                                        showStartTimePicker = false
                                    },

                                    ) {
                                    Text(
                                        "OK"
                                    )
                                }
                            },

                            dismissButton = {
                                TextButton(
                                    onClick = {

                                        showStartTimePicker = false
                                    },

                                    ) {
                                    Text(
                                        "Otkaži"
                                    )
                                }
                            },
                            title = {
                                Text(
                                    "Početak termina"
                                )
                            }
                        ) {
                            TimePicker(state = timePickerState)
                        }

                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    if (startTime != null) {
                        Button(
                            onClick = {
                                showEndTimePicker = true
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = endTime?.toString() ?: "Izaberite vreme kraja"
                            )
                        }
                    }
                    if (showEndTimePicker) {
                        val timePickerState = rememberTimePickerState()
                        TimePickerDialog(
                            onDismissRequest = { showEndTimePicker = false },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        endTime = LocalTime.of(
                                            timePickerState.hour,
                                            timePickerState.minute
                                        )
                                        showEndTimePicker = false
                                    },

                                    ) {
                                    Text(
                                        "OK"
                                    )
                                }
                            },

                            dismissButton = {
                                TextButton(
                                    onClick = {

                                        showEndTimePicker = false
                                    },

                                    ) {
                                    Text(
                                        "Otkaži"
                                    )
                                }
                            },
                            title = {
                                Text(
                                    "Kraj termina"
                                )
                            }
                        ) {
                            TimePicker(state = timePickerState)
                        }

                    }

                    val workStart = currentWorkTime.startTime
                    val workEnd = currentWorkTime.endTime
                    Log.d("StartTime",workStart.toString())

                    if (startTime != null && endTime != null &&
                        startTime!! >= workStart && endTime!! <= workEnd && startTime!! < endTime!!
                    ) {

                        Button(
                            onClick = {
                                val newTermin = Termin(
                                    id = UUID.randomUUID().toString(),
                                    patientId = patientId,
                                    doctorId = doctorId,
                                    hospitalId = hospitalId,
                                    startTime = startTime!!,
                                    endTime = endTime,
                                    date = date
                                )
                                onAddTermin(newTermin)
                                onDissmisRequest()
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Zakaži termin")
                        }
                    } else if (startTime != null && endTime != null) {
                        Text(
                            text = "Vreme mora biti u okviru radnog vremena (${workStart} - ${workEnd})",
                            color = Color.Red,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
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
}