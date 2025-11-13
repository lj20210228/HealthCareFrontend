package com.example.mobilehealthcare.ui.screens.doctor.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.magnifier
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
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonColors
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.mobilehealthcare.domain.Doctor
import com.example.mobilehealthcare.domain.Patient
import com.example.mobilehealthcare.domain.Termin
import com.example.mobilehealthcare.domain.TerminStatus
import com.example.mobilehealthcare.ui.screens.shared.CardForStatusTerminAndDate
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun DoctorHome(
    viewModel: HomeDoctorViewModel= hiltViewModel()
){
    val uiState  by viewModel.uiState.collectAsState()
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
            val numberToday=uiState.terminsWithPatient.filter {
                it.first.date== LocalDate.now()
            }.size
            val numberOnHold=uiState.terminsWithPatient.filter {
                it.first.status== TerminStatus.PENDING
            }.size
            val numberScheduled=uiState.terminsWithPatient.filter {
                it.first.status== TerminStatus.SCHEDULDED
            }.size
            DoctorHomeContent(
                terminsWithPatients = uiState.terminsWithPatient,
                doctor = uiState.doctor!!,
                numberToday = numberToday,
                numberOnHold=numberOnHold,
                numberAccepted = numberScheduled
            )
        }
    }
}





@Composable
fun DoctorHomeContent(
     terminsWithPatients:List<Pair<Termin,Patient>>,
     doctor: Doctor,
     numberToday:Int=0,
     numberOnHold:Int=0,
     numberAccepted:Int=0,
     viewModel: HomeDoctorViewModel=hiltViewModel()
){
    var selectedIndex by remember { mutableIntStateOf(0) }
    val options = listOf("Danas", "Buduci")
    Column(
        modifier = Modifier
            .background(brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFF81D4FA), // svetlo plava
                    Color(0xFF0288D1),
                ),
                center = Offset.Unspecified,
                radius = 1000f
            ))
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ){
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Dobrodošli nazad,"
                    , style = MaterialTheme.typography.headlineLarge
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = "Dr. ${doctor.fullName}"
                    , style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black.copy(alpha = 0.3f)
                )
            }
            IconButton(
                modifier = Modifier

                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(0.1f)),

                onClick = {

                }
            ){
                Icon(painter = painterResource(R.drawable.outline_notifications_24), contentDescription = null,
                    modifier = Modifier.size(32.dp), tint = MaterialTheme.colorScheme.primary)
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(vertical = 32.dp)
            ,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CardForStatusTerminAndDate(
                status = "Danas",
                number = numberToday,
                modifier = Modifier.weight(1f),
                color= MaterialTheme.colorScheme.primaryContainer
            )
            CardForStatusTerminAndDate(
                status = "Na čekanju",
                number = numberOnHold,
                modifier = Modifier.weight(1f),
                color= MaterialTheme.colorScheme.tertiaryContainer


            )
            CardForStatusTerminAndDate(
                status = "Potvrđeno",
                number = numberAccepted,
                modifier = Modifier.weight(1f),
                color= MaterialTheme.colorScheme.secondaryContainer


            )
        }

        SingleChoiceSegmentedButtonRow(
            modifier = Modifier.fillMaxWidth()
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

        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            if (selectedIndex==0){
                items(terminsWithPatients.filter {
                    it.first.date== LocalDate.now()
                }){

                    CardForTermins(
                        termin=it.first,
                        name=it.second.fullName,
                        onClickAccept = {
                            viewModel.updateTerminRequest(it.first.copy(status = TerminStatus.SCHEDULDED))
                        },
                        onClickRejected = {
                            viewModel.deleteTermin(terminId = it.first.id!!)
                        }
                    )

                }
            }
            else{
                items(terminsWithPatients){
                    it.first.date!= LocalDate.now()

                    CardForTermins(
                        termin=it.first,
                        name=it.second.fullName,
                        onClickAccept = {
                            viewModel.updateTerminRequest(it.first.copy(status = TerminStatus.SCHEDULDED))
                        },
                        onClickRejected = {
                            viewModel.deleteTermin(terminId = it.first.id!!)
                        }

                    )

                }
            }



        }


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardForTermins(
    termin: Termin,
    name: String,
    onClickAccept:()->Unit,
    onClickRejected:()-> Unit
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
            ){
                Row {
                    Icon(
                        painter =painterResource(R.drawable.profle),
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
                            style=MaterialTheme.typography.bodyMedium,
                            color =  Color.Black.copy(alpha = 0.3f)

                        )



                    }

                }
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
                        style = MaterialTheme.typography.bodySmall,
                        color=when(termin.status){
                            TerminStatus.ON_HOLD->Color(0xFFA38015)
                            TerminStatus.SCHEDULDED->Color(0xFF48BA4D)
                            TerminStatus.PENDING -> Color.Black.copy(0.7f)
                        },
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)

                    )
                }
            }
           Row (
               modifier = Modifier.padding(vertical = 24.dp, horizontal = 8.dp),
               verticalAlignment = Alignment.CenterVertically
           ){

               Icon(
                   painter = painterResource(R.drawable.outline_nest_clock_farsight_analog_24),
                   contentDescription = null,
                   modifier= Modifier.size(16.dp)

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
                       .border(2.dp,Color.Gray, RoundedCornerShape(8.dp))


               ){
                   Text(
                       text =if (termin.date== LocalDate.now())"Danas" else termin.date.toString(),
                       style = MaterialTheme.typography.bodyMedium,
                       modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                   )

               }


           }

            if (termin.status== TerminStatus.SCHEDULDED){
                Button(
                    onClick = {
                        showBottomSheet=true

                    },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                        .height(32.dp)
                        .padding(horizontal = 4.dp)
                    ,

                    colors = ButtonColors(containerColor = Color.White, contentColor = Color.Black,
                    disabledContainerColor = Color.Transparent, disabledContentColor = Color.Transparent),
                    contentPadding = PaddingValues(vertical = 0.dp, horizontal = 16.dp),
                    elevation = ButtonDefaults.buttonElevation(2.dp),
                    border = BorderStroke(1.dp,Color.Black)
                ) {
                    Text(
                        text="Detalji",
                        style = MaterialTheme.typography.bodyMedium,

                        )
                }
            }else{
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 8.dp)
                    , horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Button(
                        onClick = {
                            onClickAccept()
                        },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(32.dp)

                        ,
                        colors = ButtonColors(containerColor = Color.Black, contentColor = Color.White,
                            disabledContainerColor = Color.Transparent, disabledContentColor = Color.Transparent),
                    contentPadding = PaddingValues(vertical = 4.dp, horizontal = 12.dp),
                    elevation = ButtonDefaults.buttonElevation(0.dp),
                        border = BorderStroke(1.dp,Color.Black)
                    ) {
                        Text(
                            text="Prihvati",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.align(Alignment.CenterVertically)

                        )
                    }
                    Button(
                        onClick = {
                            onClickRejected()
                        },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(32.dp)

                        ,
                        colors = ButtonColors(containerColor = Color.White, contentColor = Color.Black,
                            disabledContainerColor = Color.Transparent, disabledContentColor = Color.Transparent),
                        contentPadding = PaddingValues(vertical = 4.dp, horizontal = 12.dp),
                        elevation = ButtonDefaults.buttonElevation(0.dp),
                        border = BorderStroke(1.dp,Color.Black)
                    ) {
                        Text(
                            text="Odbij",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.align(Alignment.CenterVertically)

                        )
                    }

                }
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
                    text="Ime pacijenta:",
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



