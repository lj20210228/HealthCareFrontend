package com.example.mobilehealthcare.ui.screens.doctor.profile

import android.app.Activity
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
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
import java.util.UUID

data class WorkTimeFormState(
    val day: DayInWeek = DayInWeek.MONDAY,
    val startTime: String = "08:00",
    val endTime: String = "16:00"
)

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfileDoctor( viewModel: ProfileDoctorViewModel = hiltViewModel()) {

    var doctor by remember { mutableStateOf<Doctor?>(null) }
    var user by remember { mutableStateOf<User?>(null) }
    var hospital by remember { mutableStateOf<Hospital?>(null) }
    var workTime by remember { mutableStateOf<List<WorkTime>>(emptyList()) }


    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedWorkTime by remember { mutableStateOf<WorkTime?>(null) }

    val openSheet: (WorkTime?) -> Unit = { wt ->
        selectedWorkTime = wt
        showBottomSheet = true
    }

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
            WorkTimeCart(
                workTimeList = workTime,
                onEditClick = { openSheet(it) },
                onDeleteClick = { viewModel.deleteDoctorWorkTime(it.id) }, // Poziv brisanja
                onAddClick = { openSheet(null) }
            )
        }

        Spacer(Modifier.size(32.dp))

        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = { viewModel.logout()

            },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text("Izloguj se")
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            WorkTimeSheetContent(
                initialWorkTime = selectedWorkTime,
                onSave = { day, startStr, endStr ->
                    try {
                        val sTime = LocalTime.parse(startStr)
                        val eTime = LocalTime.parse(endStr)

                        val wt = WorkTime(
                            id = selectedWorkTime?.id ?: UUID.randomUUID().toString(),
                            doctorId = viewModel.doctorId ?: "",
                            dayIn = day,
                            startTime = sTime,
                            endTime = eTime
                        )

                        if (selectedWorkTime == null) {
                            viewModel.addDoctorWorkTime(wt)
                        } else {
                            viewModel.updateDoctorWorkTime(wt)
                        }
                        showBottomSheet = false
                    } catch (e: Exception) {
                        Toast.makeText(
                            context,
                            "Neispravan format vremena (HH:mm)",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                onCancel = { showBottomSheet = false },
                existingDays = workTime.map { it.dayIn }
            )
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
fun WorkTimeSheetContent(
    initialWorkTime: WorkTime?,
    onSave: (DayInWeek, String, String) -> Unit,
    onCancel: () -> Unit,
    existingDays: List<DayInWeek>
) {


    val availableDays = DayInWeek.values().filter {
        it !in existingDays || it == initialWorkTime?.dayIn
    }

    var day by remember { mutableStateOf(initialWorkTime?.dayIn ?: availableDays.firstOrNull() ?: DayInWeek.MONDAY) }
    var startTime by remember { mutableStateOf(initialWorkTime?.startTime ?: LocalTime.of(8, 0)) }
    var endTime by remember { mutableStateOf(initialWorkTime?.endTime ?: LocalTime.of(16, 0)) }

    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp).padding(bottom = 32.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(text = if (initialWorkTime == null) "Dodaj radno vreme" else "Izmeni radno vreme", style = MaterialTheme.typography.headlineSmall)

        Text("Dan u nedelji:", style = MaterialTheme.typography.labelLarge)
        Row(Modifier.horizontalScroll(rememberScrollState())) {
            availableDays.forEach { d ->
                FilterChip(
                    selected = day == d,
                    onClick = { day = d },
                    label = { Text(d.name.lowercase().replaceFirstChar { it.uppercase() }) },
                    modifier = Modifier.padding(end = 4.dp)
                )
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            OutlinedButton(
                onClick = {
                    showTimePickerDialog(context, startTime) { startTime = it }
                },
                modifier = Modifier.weight(1f)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Početak", style = MaterialTheme.typography.labelSmall)
                    Text(startTime.toString())
                }
            }

            OutlinedButton(
                onClick = {
                    showTimePickerDialog(context, endTime) { endTime = it }
                },
                modifier = Modifier.weight(1f)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Kraj", style = MaterialTheme.typography.labelSmall)
                    Text(endTime.toString())
                }
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TextButton(onClick = onCancel, modifier = Modifier.weight(1f)) {
                Text("Otkaži")
            }
            Button(
                onClick = { onSave(day, startTime.toString(), endTime.toString()) },
                modifier = Modifier.weight(1f)
            ) {
                Text("Sačuvaj")
            }
        }
    }
}

fun showTimePickerDialog(context: Context, currentTime: LocalTime, onTimeSelected: (LocalTime) -> Unit) {
    TimePickerDialog(
        context,
        { _, hour, minute -> onTimeSelected(LocalTime.of(hour, minute)) },
        currentTime.hour,
        currentTime.minute,
        true
    ).show()
}

@Composable
fun WorkTimeCart(
    workTimeList: List<WorkTime>,
    onEditClick: (WorkTime) -> Unit,
    onDeleteClick: (WorkTime) -> Unit, // Novo
    onAddClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.secondary)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Radno vreme", style = MaterialTheme.typography.titleSmall)
            // Dugme za dodavanje je omogućeno samo ako lekar nema popunjenih svih 7 dana
            if (workTimeList.size < 7) {
                IconButton(onClick = onAddClick) {
                    Icon(painter = painterResource(R.drawable.outline_add_24), contentDescription = "Dodaj")
                }
            }
        }

        if (workTimeList.isEmpty()) {
            Text(
                text = "Nema definisanog radnog vremena",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = androidx.compose.ui.graphics.Color.Gray
            )
        } else {
            workTimeList.forEach { wt ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val dayName = when(wt.dayIn) {
                        DayInWeek.MONDAY -> "Ponedeljak"
                        DayInWeek.TUESDAY -> "Utorak"
                        DayInWeek.WEDNESDAY -> "Sreda"
                        DayInWeek.THURSDAY -> "Četvrtak"
                        DayInWeek.FRIDAY -> "Petak"
                        DayInWeek.SATURDAY -> "Subota"
                        DayInWeek.SUNDAY -> "Nedelja"
                    }

                    Text(text = dayName, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
                    Text(text = "${wt.startTime} - ${wt.endTime}", style = MaterialTheme.typography.bodySmall)

                    IconButton(onClick = { onEditClick(wt) }) {
                        Icon(painter = painterResource(R.drawable.outline_edit_24), contentDescription = "Izmeni", modifier = Modifier.size(16.dp))
                    }
                    IconButton(onClick = { onDeleteClick(wt) }) {
                        Icon(painter = painterResource(R.drawable.outline_cleaning_services_24), contentDescription = "Obriši", modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
fun ProfileDoctorPreview(){
    ProfileDoctor()
}