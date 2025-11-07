package com.example.mobilehealthcare.ui.screens.patient.home

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.mobilehealthcare.R
import com.example.mobilehealthcare.domain.Patient
import com.example.mobilehealthcare.domain.Recipe
import com.example.mobilehealthcare.domain.Termin
import com.example.mobilehealthcare.domain.TerminStatus
import java.time.LocalDate
import java.time.LocalTime

@SuppressLint("SuspiciousIndentation")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomePatientScreen(
    viewModel: HomePatientViewModel= hiltViewModel()
) {
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
        }else->{
            HomePatientContent(
                patient = uiState.patient!!,
                termins = uiState.termins,
                recipes = uiState.recipes
            )
        }
    }
}
@Composable
fun HomePatientContent(
    patient: Patient,
    recipes: List<Recipe>,
    termins: List<Termin>
){
    var showAll by remember { mutableStateOf(false) }
    var showAllRecipes by remember { mutableStateOf(false) }
    val displayedTermins=if (showAll)termins else termins.take(2)
    val displayedRecipes=if (showAllRecipes)recipes else recipes.take(2)


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFF81D4FA), Color(0xFF0288D1)),
                    center = Offset.Unspecified,
                    radius = 1000f
                )
            )
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Dobrodošli nazad,", style = MaterialTheme.typography.headlineLarge)
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        patient.fullName,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Black.copy(alpha = 0.3f)
                    )
                }
                IconButton(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(0.1f)),
                    onClick = {}
                ) {
                    Icon(
                        painter = painterResource(R.drawable.outline_notifications_24),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Spacer(modifier = Modifier.size(32.dp))
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(
                    onClick = {},
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onBackground
                    ),
                    modifier = Modifier.height(84.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("+", style = MaterialTheme.typography.displaySmall)
                        Text("Zakaži Novi Termin", style = MaterialTheme.typography.bodyMedium)
                    }
                }

                OutlinedButton(
                    onClick = {},
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    ),
                    modifier = Modifier.height(84.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(R.drawable.calendar),
                            contentDescription = null,
                            modifier = Modifier.size(36.dp)
                        )
                        Text(
                            "Vidi sve termine",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.size(24.dp))
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Nadolazeći termini",
                    style = MaterialTheme.typography.labelLarge
                )
                TextButton(onClick = { showAll = !showAll }) {
                    Text(
                        if (showAll) "Vidi manje" else "Vidi sve",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            Spacer(modifier = Modifier.size(16.dp))
        }


        if (displayedTermins.isNotEmpty()){
            items(displayedTermins) { termin ->
                CardForTerminsPatients(
                    termin = termin,
                    name = "Petar Petrovic"
                )
            }
        }else{
            item {
                Text(
                    "Nemate zakazanih termina",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }


        item {
            Spacer(modifier = Modifier.size(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Vaši recepti",
                    style = MaterialTheme.typography.labelLarge,
                    textAlign = TextAlign.Center
                )
                TextButton(onClick = { showAllRecipes = !showAllRecipes }) {
                    Text(
                        if (showAllRecipes) "Vidi manje" else "Vidi sve",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

            }
            Spacer(modifier = Modifier.size(16.dp))
        }
        if (displayedRecipes.isNotEmpty()){
            items(displayedRecipes) { recipe ->
                RecipeCart(
                    recipe = recipe,
                    doctorName = "Ana Ivanovic",
                )
            }
        }else{
            item {
                Text(
                    "Nemate recepata",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }



        item { Spacer(modifier = Modifier.size(40.dp)) }
    }
}
@Composable
fun RecipeCart(
    recipe: Recipe,
    doctorName: String
){
    Card(
        modifier = Modifier.fillMaxWidth()
            .padding(vertical = 8.dp)
            .height(70.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ){
        Row(
            modifier = Modifier.fillMaxSize()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.capsules),
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp)
            )
            Column(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    text = recipe.medication,
                    style = MaterialTheme.typography.labelLarge
                )
                Text(
                    text="Prepisao: ${doctorName}",
                    style = MaterialTheme.typography.bodySmall

                )
            }
            Spacer(Modifier.weight(1f))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = "Važi do",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = recipe.dateExpired.toString(),
                    modifier = Modifier
                        .border(2.dp, Color.Gray,RoundedCornerShape(8.dp))
                        .padding(6.dp)

                    ,
                    style = MaterialTheme.typography.labelSmall
                )
            }


        }

    }
}




@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardForTerminsPatients(
    termin: Termin,
    name: String,
    ){

    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(210.dp)
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, color = Color.Gray.copy(0.4f), shape = RoundedCornerShape(8.dp))
        ,
        colors= CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    Icon(
                        painter = painterResource(R.drawable.profle),
                        contentDescription = null,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)

                    )
                    Column(

                        modifier = Modifier
                            .padding(horizontal = 8.dp)


                    ) {
                        Text(
                            text = name,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = termin.desctiption!!,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black.copy(alpha = 0.3f)

                        )


                    }

                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            color = when (termin.status) {
                                TerminStatus.ON_HOLD -> {
                                    Color.Yellow.copy(0.15f)
                                }

                                TerminStatus.SCHEDULDED -> Color.Green.copy(0.15f)
                                TerminStatus.PENDING -> Color.Gray.copy(0.2f)
                            }
                        )
                ) {
                    Text(
                        text = when (termin.status) {
                            TerminStatus.ON_HOLD -> {
                                "Na čekanju"
                            }

                            TerminStatus.SCHEDULDED -> "Povrđen"
                            TerminStatus.PENDING -> "Primljen"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = when (termin.status) {
                            TerminStatus.ON_HOLD -> Color(0xFFA38015)
                            TerminStatus.SCHEDULDED -> Color(0xFF48BA4D)
                            TerminStatus.PENDING -> Color.Black.copy(0.7f)
                        },
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)

                    )
                }
            }
            Row(
                modifier = Modifier.padding(vertical = 24.dp, horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    painter = painterResource(R.drawable.outline_nest_clock_farsight_analog_24),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)

                )
                Text(
                    text = termin.startTime.toString(),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 2.dp)

                )
                Text(
                    " - "
                )
                Text(
                    text = termin.endTime.toString(),
                    style = MaterialTheme.typography.bodySmall,
                )
                Box(
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .border(2.dp, Color.Gray, RoundedCornerShape(8.dp))


                ) {
                    Text(
                        text =if (termin.date== LocalDate.now())"Danas" else termin.date.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )

                }


            }


            Button(
                onClick = {
                    showBottomSheet = true

                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
                    .height(32.dp)
                    .padding(horizontal = 4.dp),

                colors = ButtonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black,
                    disabledContainerColor = Color.Transparent,
                    disabledContentColor = Color.Transparent
                ),
                contentPadding = PaddingValues(vertical = 0.dp, horizontal = 16.dp),
                elevation = ButtonDefaults.buttonElevation(2.dp),
                border = BorderStroke(1.dp, Color.Black)
            ) {
                Text(
                    text = "Detalji",
                    style = MaterialTheme.typography.bodyMedium,

                    )
            }


        }




    }
    if(showBottomSheet) {
        PartialBottomSheet(
            termin = termin,
            sheetState = sheetState,
            onDissmisRequest = {showBottomSheet=false},
            name=name
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartialBottomSheet(
    termin: Termin,
    sheetState: SheetState,
    onDissmisRequest:()-> Unit,
    name: String
) {
    ModalBottomSheet(
        modifier = Modifier.fillMaxHeight(),
        sheetState = sheetState,
        onDismissRequest = { onDissmisRequest() }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            Text(
                text=termin.desctiption.toString(),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.size(16.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically

            ) {
                Icon(
                    painter = painterResource(R.drawable.patient),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text="Ime lekara:",
                    style = MaterialTheme.typography.bodyLarge,

                    )
                Text(
                    text=name,
                    style = MaterialTheme.typography.labelLarge
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically

            ) {

                Icon(
                    painter = painterResource(R.drawable.calendar),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text="Datum:",
                    style = MaterialTheme.typography.bodyLarge,

                    )
                Text(
                    text=termin.date.toString(),
                    style = MaterialTheme.typography.labelLarge
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically

            ) {
                Icon(
                    painter = painterResource(R.drawable.outline_nest_clock_farsight_analog_24),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text="Početak termina:",
                    style = MaterialTheme.typography.bodyLarge,

                    )
                Text(
                    text=termin.startTime.toString(),
                    style = MaterialTheme.typography.labelLarge
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically

            ) {
                Icon(
                    painter = painterResource(R.drawable.outline_nest_clock_farsight_analog_24),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text="Kraj termina:",
                    style = MaterialTheme.typography.bodyLarge,

                    )
                Text(
                    text=termin.endTime.toString(),
                    style = MaterialTheme.typography.labelLarge
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
                , verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text="Status termina:",
                    style = MaterialTheme.typography.bodyLarge,

                    )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(color=when(termin.status){
                            TerminStatus.ON_HOLD->{
                                Color.Yellow.copy(0.15f)}
                            TerminStatus.SCHEDULDED->Color.Green.copy(0.15f)
                            TerminStatus.PENDING ->Color.Gray.copy(0.2f)
                        })
                ){
                    Text(
                        text=when(termin.status){
                            TerminStatus.ON_HOLD->{
                                "Na čekanju"}
                            TerminStatus.SCHEDULDED->"Povrđen"
                            TerminStatus.PENDING -> "Primljen"
                        },
                        style = MaterialTheme.typography.labelLarge,
                        color=when(termin.status){
                            TerminStatus.ON_HOLD->Color(0xFFA38015)
                            TerminStatus.SCHEDULDED->Color(0xFF48BA4D)
                            TerminStatus.PENDING -> Color.Black.copy(0.7f)
                        },
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)

                    )
                }
            }


        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
fun HomePatientPreview(){


    HomePatientContent(patient= Patient(
        fullName = "Petar Petrovic",
        userId = "2",
        hospitalId = "2",
        jmbg = "100310220318",
    ),emptyList(),emptyList())
}
