package com.example.mobilehealthcare.ui.screens.doctor.patients

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.mobilehealthcare.R
import com.example.mobilehealthcare.domain.Patient
import com.example.mobilehealthcare.domain.Recipe
import java.time.LocalDate
import java.util.UUID

data class RecipeFormState(
    val medication: String = "",
    val quantity: Int = 0,
    val instructions: String = "",
    val dateExpired: LocalDate = LocalDate.now().plusMonths(1)
)

@Composable
fun DoctorPatientScreen(
    viewModel: PatientsViewModel= hiltViewModel(),
    ){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if(uiState.isLoading){
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }
    }
    if (uiState.error!=null){
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = uiState.error!!
            )
        }
    }
    DoctorPatientsScreenContent(
        patients = uiState.patients,
        currentDoctorId = uiState.doctorId!!
        , onRecipeAdded = {recipe -> viewModel.addRecipe(recipe)}
    )

}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DoctorPatientsScreenContent(

    patients:List<PatientWithRecipe>,
    currentDoctorId: String,
    onRecipeAdded:(Recipe)-> Unit
) {
    var searchQuery by remember { mutableStateOf("") }



    val filteredPatients = patients.filter { it.patient.fullName.contains(searchQuery, ignoreCase = true) }

    var showRecipeDialogFor by remember { mutableStateOf<Patient?>(null) }
    var showDetailsDialogFor by remember { mutableStateOf<PatientWithRecipe?>(null) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Moji pacijenti", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Text("Pregled i upravljanje receptima", color = Color.Gray)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Pretraži po imenu...") },
                leadingIcon = { Icon(painter = painterResource(R.drawable.outline_search_24), contentDescription = null) },
                shape = RoundedCornerShape(16.dp)
            )

            filteredPatients.forEach { patient ->
                PatientCard(
                    patient = patient,
                    onDetailsClick = { showDetailsDialogFor = patient },
                    onRecipeClick = { showRecipeDialogFor = patient.patient }
                )
            }
        }
    }

    showRecipeDialogFor?.let { patient ->
        AddRecipeDialog(
            patient = patient,
            doctorId = currentDoctorId,
            onDismiss = { showRecipeDialogFor = null },
            onConfirm = { newRecipe ->
                onRecipeAdded(newRecipe)
               showRecipeDialogFor = null
            }
        )
    }

    showDetailsDialogFor?.let { patient ->
        PatientDetailsDialog(patient = patient, onDismiss = { showDetailsDialogFor = null })
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddRecipeDialog(
    patient: Patient,
    doctorId: String,
    onDismiss: () -> Unit,
    onConfirm: (Recipe) -> Unit
) {
    var formState by remember { mutableStateOf(RecipeFormState()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Novi recept: ${patient.fullName}") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = formState.medication,
                    onValueChange = { formState = formState.copy(medication = it) },
                    label = { Text("Naziv leka") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = formState.quantity.toString(),
                    onValueChange = { formState = formState.copy(quantity = it.toIntOrNull()?:0) },
                    label = { Text("Količina (upisite samo broj kutija)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )

                OutlinedTextField(
                    value = formState.instructions,
                    onValueChange = { formState = formState.copy(instructions = it) },
                    label = { Text("Uputstvo za upotrebu") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )

                Text(
                    text = "Važi do: ${formState.dateExpired}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val recipe = Recipe(
                        patientId = patient.id!!,
                        doctorId = doctorId,
                        medication = formState.medication,
                        quantity = formState.quantity.toInt(),
                        instructions = formState.instructions,
                        dateExpired = formState.dateExpired
                    )
                    onConfirm(recipe)
                },
                enabled = formState.medication.isNotBlank()
            ) {
                Text("Izdaj recept")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Otkaži") }
        }
    )
}
@Composable
fun PatientCard(patient: PatientWithRecipe, onDetailsClick: () -> Unit, onRecipeClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(patient.patient.fullName, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text("JMBG: ${patient.patient.jmbg}", style = MaterialTheme.typography.bodyMedium)
            }

            Row {
                IconButton(onClick = onDetailsClick) {
                    Icon(painter = painterResource(R.drawable.outline_info_24), contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                }
                IconButton(onClick = onRecipeClick) {
                    Icon(painter = painterResource(R.drawable.capsules), contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                }
            }
        }
    }
}

@Composable
fun PatientDetailsDialog(patient: PatientWithRecipe, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Podaci o pacijentu") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                DetailItem("Ime i prezime", patient.patient.fullName)
                DetailItem("JMBG", patient.patient.jmbg)
                Text("Recepti:")

                patient.recipes.map {
                    DetailItem("Lek",it.medication+
                            " "+it.quantity+" Kutije")
                }

            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Zatvori") }
        }
    )
}

@Composable
fun DetailItem(label: String, value: String) {
    Column {
        Text(label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        Text(value, style = MaterialTheme.typography.bodyLarge)
    }
}