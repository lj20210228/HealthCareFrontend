package com.example.mobilehealthcare.ui.screens.patient.recipes

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonColors
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mobilehealthcare.R
import com.example.mobilehealthcare.domain.Recipe
import com.example.mobilehealthcare.ui.screens.patient.home.RecipeCart
import dagger.Provides
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PatientRecipesScreen(){
    var selectedIndex by remember { mutableStateOf(0) }
    val options=listOf("Aktivni recepti","Istekli")

    val recipe1= Recipe(
        patientId = "1",
        doctorId = "1",
        medication = "Brufen",
        quantity ="2",
        instructions = "Jedan ujutru i 1 uvece",
        dateExpired = LocalDate.of(2025,12,12),
        id = "1"
    )
    val recipe2= Recipe(
        patientId = "1",
        doctorId = "1",
        medication = "Tegretol",
        quantity ="2",
        instructions = "Jedan ujutru i 1 uvece",
        dateExpired = LocalDate.of(2025,12,12),
        id = "1"
    )
    val recipe3= Recipe(
        patientId = "1",
        doctorId = "1",
        medication = "Amoksicilin",
        quantity ="2",
        instructions = "Jedan ujutru i 1 uvece",
        dateExpired = LocalDate.of(2025,9,12),
        id = "1"
    )
    var recipes=listOf(recipe1,recipe2,recipe3)
    LazyColumn(
        modifier = Modifier.fillMaxSize()
            .background(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFF81D4FA), Color(0xFF0288D1)),
                center = Offset.Unspecified,
                radius = 1000f
            )
        )
            .padding(16.dp),

    ) {
        item {

            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Column {
                    Text(
                        text = "Moji Recepti",
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Text(
                        text = "Pratite svoju terapiju",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.Black.copy(0.4f)
                    )
                }
                Image(
                    painter = painterResource(R.drawable.capsules),
                    modifier = Modifier.size(48.dp),
                    contentDescription = null
                )
            }


        }
        item {
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                options.forEachIndexed { index,label->
                    SegmentedButton(
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index },
                        label = { Text(text = label) },
                        icon = {},
                       modifier = Modifier.fillMaxWidth(),
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = options.size
                        ),
                        colors = SegmentedButtonColors(
                            activeContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            activeContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            activeBorderColor = Color.LightGray,
                            inactiveContainerColor = MaterialTheme.colorScheme.inversePrimary,
                            inactiveContentColor = MaterialTheme.colorScheme.inverseSurface,
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

        }
        if (selectedIndex==0){
            items(recipes.filter { it.dateExpired.isAfter(LocalDate.now()) }){
                RecipeCartBig(
                    it,
                    "Petar Jovanovic"
                )
            }
        }else{
            items(recipes.filter { it.dateExpired.isBefore(LocalDate.now()) }){
                RecipeCartBig(
                    it,
                    "Petar Jovanovic"
                )
            }
        }



    }
}

@Composable
fun RecipeCartBig(
    recipe: Recipe,
    nameOfDoctor: String
){
    Card(
        modifier = Modifier.fillMaxWidth()
            .padding(vertical = 8.dp)
    ){
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp)
        ) {
            Row {
                Image(
                    painter = painterResource(R.drawable.capsules)
                    , contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                )
                Spacer(modifier = Modifier.size(16.dp))
                Text(
                    text=recipe.medication,
                    style= MaterialTheme.typography.titleLarge
                )

            }
            Spacer(modifier = Modifier.size(24.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.calendar),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Datum isteka:",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = recipe.dateExpired.toString(),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Spacer(Modifier.size(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.stethoscope),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Prepisao:",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = nameOfDoctor,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Spacer(Modifier.size(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth()
                    .border(2.dp,Color.Blue, RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Blue.copy(0.2f)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.outline_info_24),
                    contentDescription = null,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
                Text(
                    text = recipe.instructions,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Blue
                )
            }


        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
fun RecipesPreview(){
    val recipe1= Recipe(
        patientId = "1",
        doctorId = "1",
        medication = "Brufen",
        quantity ="2",
        instructions = "Jedan ujutru i 1 uvece",
        dateExpired = LocalDate.of(2025,12,12),
        id = "1"
    )

    PatientRecipesScreen()
}