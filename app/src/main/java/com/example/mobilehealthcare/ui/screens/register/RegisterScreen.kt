package com.example.mobilehealthcare.ui.screens.register

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.compose.primaryLight
import com.example.mobilehealthcare.R
import com.example.mobilehealthcare.domain.Doctor
import com.example.mobilehealthcare.domain.Hospital
import com.example.mobilehealthcare.domain.Patient
import com.example.mobilehealthcare.domain.Role
import com.example.mobilehealthcare.domain.User
import com.example.mobilehealthcare.models.request.RegisterRequest
import com.example.mobilehealthcare.ui.screens.AuthState
import com.example.mobilehealthcare.ui.screens.AuthViewModel
import com.example.mobilehealthcare.ui.screens.Screen

private val passwordRegex = Regex("^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#\$%^&*()_+\\-={}\\[\\]:;\"'<>,.?/]).{8,}\$")
fun isValidPassword(password: String): Boolean {
    return passwordRegex.matches(password)
}

@Composable
fun RegisterScreen(
    modifier: Modifier,
    viewModel: AuthViewModel=hiltViewModel(),
    navController: NavController
) {
    val context = LocalContext.current
    val hospitals by viewModel.hospitalsState.collectAsState()


    var selectedHospital by remember { mutableStateOf<Hospital?>(null) }


    LaunchedEffect(Unit) {
        viewModel.getAllHospitals()
    }
    Log.d("Hospitals",hospitals.toString())



    val uiState = viewModel.uiState.collectAsState()
    var name by remember { mutableStateOf("") }
    var jmbg by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var specialization by remember { mutableStateOf("") }
    var isGeneral by remember { mutableStateOf(false) }
    var maxPatients by remember { mutableStateOf("") }
    var selectedOption by remember { mutableStateOf(0) }
    val options = listOf("Lekar", "Pacijent")




    Column(
        modifier = modifier.fillMaxSize()
            .background( color = primaryLight)
            .verticalScroll(rememberScrollState())
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {



        Text(
            style = MaterialTheme.typography.titleLarge,
            text = "Registruj se",
            modifier= Modifier.padding(vertical = 32.dp)
        )
        SingleChoiceSegmentedButtonRow(
        ){
            options.forEachIndexed { index, string ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = options.size
                    ),

                    onClick = { selectedOption = index },
                    selected = index == selectedOption, label = { Text(string) }

                )
            }
        }
        Spacer(
            modifier = Modifier.size(32.dp)
        )
        HospitalDropdown(
            hospitals = hospitals.hospitals,
            selectedHospital = selectedHospital?.name ?: "",
            onHospitalSelected = { selectedHospital = it },

        )
        if (selectedOption == 0) {
            RegisterDoctor(
                name = name,
                onNameChage = { name = it },
                email = email,
                onEmailChange = { email = it },
                password = password,
                onPasswordChange = { password = it },
                specialization = specialization,
                onChangeSpecialization = { specialization = it },
                isGeneral = isGeneral,
                isGeneralChange = { isGeneral = it },
                maxPatients = maxPatients,
                onChangeMaxPatients = { newValue ->
                    if (newValue.all { it.isDigit() }) maxPatients = newValue
                },
                onClick = {
                    if (selectedHospital == null) {
                        Toast.makeText(context, "Izaberite bolnicu", Toast.LENGTH_SHORT).show()
                        return@RegisterDoctor
                    }
                    val req = RegisterRequest(
                        doctor = Doctor(
                            fullName = name,
                            specialization = if (isGeneral)"Lekar opste prakse" else specialization,
                            isGeneral = isGeneral,
                            maxPatients = maxPatients.toInt(),
                            hospitalId = selectedHospital!!.id,
                        ),
                        user = User(
                            email = email,
                            password = password,
                            role = Role.ROLE_DOCTOR
                        )

                    )
                    viewModel.register(req)
                }
            )
        } else {
            RegisterPatient(
                name = name,
                onNameChage = { name = it },
                email = email,
                onEmailChange = { email = it },
                password = password,
                onPasswordChange = { password = it },
                jmbg = jmbg,
                onJmbgChange = { newValue ->
                    if (newValue.all { it.isDigit() } && newValue.length <= 13) jmbg = newValue
                },
                onClick = {
                    if (selectedHospital == null) {
                        Toast.makeText(context, "Izaberite bolnicu", Toast.LENGTH_SHORT).show()
                        return@RegisterPatient
                    }
                    val req = RegisterRequest(
                        patient = Patient(
                            fullName = name,
                            jmbg = jmbg,
                            hospitalId = selectedHospital!!.id,

                            ),
                        user = User(
                            email = email,
                            password = password,
                            role = Role.ROLE_PATIENT
                        )

                    )
                    viewModel.register(req)
                }
            )
        }
        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center

        ){
            Text(
                text = "Imate nalog?",
                style = MaterialTheme.typography.bodyMedium,
            )

            TextButton(
                onClick = { navController.navigate("login") }
            ) {
                Text(
                    text = "Ulogujte se",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

        }

    }
    when (val state = uiState.value) {
        is AuthState.Success -> {
            LaunchedEffect(Unit) {
                Toast.makeText(context, "Uspešna registracija", Toast.LENGTH_SHORT).show()


            }
        }

        is AuthState.Error -> {
            Toast.makeText(context, state.message ?: "Greška pri registraciji", Toast.LENGTH_SHORT)
                .show()
        }

        AuthState.Nothing -> Unit
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HospitalDropdown(
    hospitals: List<Hospital>,
    selectedHospital: String,
    onHospitalSelected: (Hospital) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp)) {
        Text(
            text = "Izaberi bolnicu",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        ExposedDropdownMenuBox(

            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
        ) {
            OutlinedTextField(
                value = selectedHospital,
                onValueChange = {onHospitalSelected},
                readOnly = true,
                label = { Text("Bolnica") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                hospitals.forEach { hospital ->
                    DropdownMenuItem(
                        text = { Text(hospital.name) },
                        onClick = {
                            onHospitalSelected(hospital)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
@Composable
fun RegisterPatient(
     name: String,
     onNameChage:(String)-> Unit,
     password:String,
     onPasswordChange:(String)->Unit,
     jmbg:String,
     onJmbgChange:(String)->Unit,
     email:String,
     onEmailChange:(String)-> Unit,
     onClick:()-> Unit,



){
    var passwordVisible by remember { mutableStateOf(false) }
    val isPasswordValid = isValidPassword(password)

    Column(
        modifier= Modifier.fillMaxWidth()
            .padding(32.dp),

        ){
        Text(
            text = "Ime i prezime",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(4.dp)
        )
        OutlinedTextField(
            value = name,
            onValueChange = onNameChage,
            shape = RoundedCornerShape(16.dp),

            modifier= Modifier
                .fillMaxWidth()
                ,
            placeholder = {
                Text("Unesite puno ime i prezime")
            }

        )
        Spacer(Modifier.size(16.dp))
        Text(
            "Email",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(4.dp)


        )
        OutlinedTextField(
            value = email,
            shape = RoundedCornerShape(16.dp),
            modifier= Modifier
                .fillMaxWidth()
                ,
            onValueChange = onEmailChange,
            placeholder = {
                Text("Unesite email")
            }

        )
        Spacer(Modifier.size(16.dp))

        Text(
            "JMBG",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(4.dp)


        )
        OutlinedTextField(
            value = jmbg,
            onValueChange = onJmbgChange,
            shape = RoundedCornerShape(16.dp),

            modifier= Modifier
                .fillMaxWidth()
              ,
            placeholder = {
                Text("Unesite vas jmbg, tacno 13 cifara")
            }

        )
        Spacer(Modifier.size(16.dp))

        Text(
            "Lozinka",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(4.dp)


        )
        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            shape = RoundedCornerShape(16.dp),

            modifier= Modifier
                .fillMaxWidth()
                ,
            placeholder = { Text("Unesite lozinku") },
            label = { Text("Lozinka") },
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible)
                    painterResource(id = R.drawable.outline_lock_24)
                else
                    painterResource(id = R.drawable.outline_lock_open_24)

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(painter = image, contentDescription = if (passwordVisible) "Sakrij lozinku" else "Prikaži lozinku")
                }
            },
        )
        if (password.isNotEmpty() && !isPasswordValid) {
            Text(
                text = "Lozinka mora imati veliko slovo, broj i specijalan karakter.",
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        Button(
            modifier= Modifier.padding(vertical = 32.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(shape = RoundedCornerShape(16.dp), border = BorderStroke(1.dp,Color.White))
                .background(MaterialTheme.colorScheme.secondary)
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            onClick = {onClick()},
            enabled = email.isNotEmpty()&&password.isNotEmpty()&&jmbg.isNotEmpty()&&name.isNotEmpty()&&jmbg.length==13&&isPasswordValid,
            colors = ButtonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer, disabledContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                contentColor =Color.White,
                disabledContentColor = Color.White
            ),
        ) {
            Text(
                "Registruj se"
            )

        }

    }
}
@Composable
fun RegisterDoctor(
    name: String,
    onNameChage:(String)-> Unit,
    password:String,
    onPasswordChange:(String)->Unit,
    specialization:String,
    onChangeSpecialization: (String)-> Unit,
    isGeneral:Boolean=false,
    isGeneralChange:(Boolean)-> Unit,
    email:String,
    onEmailChange:(String)-> Unit,
    onClick:()-> Unit,
    maxPatients: String,
    onChangeMaxPatients:(String)-> Unit


){
    var passwordVisible by remember { mutableStateOf(false) }
    val isPasswordValid = isValidPassword(password)

    Column(
        modifier= Modifier.fillMaxWidth()
            .padding(32.dp),

        ){
        Text(
            text = "Ime i prezime",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(4.dp)

        )
        OutlinedTextField(
            value = name,
            onValueChange = onNameChage,
            shape = RoundedCornerShape(16.dp),

            modifier= Modifier
                .fillMaxWidth(),
            placeholder = {
                Text("Unesite puno ime i prezime")
            }

        )
        Spacer(Modifier.size(16.dp))
        Text(
            "Email",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(4.dp)


        )
        OutlinedTextField(
            value = email,
            shape = RoundedCornerShape(16.dp),
            modifier= Modifier
                .fillMaxWidth()
             ,
            onValueChange = onEmailChange,
            placeholder = {
                Text("Unesite email")
            }

        )

        Spacer(Modifier.size(16.dp))

        Text(
            "Lozinka",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(4.dp)


        )
        if (password.isNotEmpty() && !isPasswordValid) {
            Text(
                text = "Lozinka mora imati veliko slovo, broj i specijalan karakter.",
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            shape = RoundedCornerShape(16.dp),

            modifier= Modifier
                .fillMaxWidth(),
            placeholder = { Text("Unesite lozinku") },
            label = { Text("Lozinka") },
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible)
                    painterResource(id = R.drawable.outline_lock_24)
                else
                    painterResource(id = R.drawable.outline_lock_open_24)

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(painter = image, contentDescription = if (passwordVisible) "Sakrij lozinku" else "Prikaži lozinku")
                }
            },
        )
        Spacer(Modifier.size(16.dp))


        if (!isGeneral){
            Text(
                "Specijalizacija",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(4.dp)


            )
            OutlinedTextField(
                value = specialization,
                onValueChange = onChangeSpecialization,
                shape = RoundedCornerShape(16.dp),

                modifier= Modifier
                    .fillMaxWidth(),
                placeholder = {
                    Text("Unesite naziv specijalizacije")
                },


            )
            Spacer(Modifier.size(16.dp))
        }



        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                "Lekar opste prakse",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(4.dp)

            )
            Switch(
                checked = isGeneral,
                onCheckedChange = isGeneralChange

            )
        }

        Spacer(Modifier.size(16.dp))


        Text(
            "Maksimalan broj pacijenata kojima mozete biti izabrani lekar",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(4.dp)

        )
        OutlinedTextField(
            value =maxPatients ,
            onValueChange = onChangeMaxPatients,
            shape = RoundedCornerShape(16.dp),

            modifier= Modifier
                .fillMaxWidth()
               ,
            placeholder = {
                Text("Unesite maksimalan broj pacijenata")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )

        )
        Spacer(Modifier.size(16.dp))


        Button(
            modifier= Modifier.padding(vertical = 32.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(shape = RoundedCornerShape(16.dp), border = BorderStroke(1.dp,Color.White))
                .background(MaterialTheme.colorScheme.primary)
                .fillMaxWidth(),
            colors = ButtonColors(
                containerColor = MaterialTheme.colorScheme.secondary, disabledContainerColor = MaterialTheme.colorScheme.onSecondary,
                contentColor =Color.White,
                disabledContentColor = Color.White
            ),
            shape = RoundedCornerShape(16.dp),
            onClick = {onClick()},
            enabled = email.isNotEmpty() && password.isNotEmpty() &&(isGeneral==true || specialization.isNotEmpty())&&maxPatients.isNotEmpty()&&isPasswordValid
        ) {
            Text(
                "Registruj se"
            )

        }

    }
}
