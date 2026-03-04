package ngo.friendship.mhealth.dc.presentation.screens.main.case.components.case_detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ngo.friendship.mhealth.dc.theme.UnfocusedBorderColor

enum class MealTime {
    AGE,   // আগে
    PORE   // পরে
}

@Composable
fun MedicineAddScreen() {
    var selectedType by remember { mutableStateOf("Cap") }
    var selectedMed by remember { mutableStateOf("Amoxicillin 500") }
    var brand by remember { mutableStateOf("") }
    var selectedDose by remember { mutableStateOf("1+0+1") }
    var selectedDuration by remember { mutableStateOf("৭ দিন") }
    var selectedTiming by remember { mutableStateOf("খাবারের পরে") }


    val list = remember { mutableStateListOf<MedItem>() }

    Column(Modifier.fillMaxSize()) {

        MedicineComposerCard(
            onAddClick = {
                list.add(
                    MedItem(
                        type = selectedType,
                        name = selectedMed,
                        brand = brand.trim(),
                        dose = selectedDose,
                        duration = selectedDuration,
                        timing = selectedTiming
                    )
                )
                brand = "" // optional clear
            }
        )

        Spacer(Modifier.height(10.dp))


    }
}


@Composable
fun MedicineComposerCard(
    onAddClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(width = 1.dp, color = UnfocusedBorderColor),
        tonalElevation = 0.dp
    ) {
        Column(Modifier.padding(12.dp)) {

            var doseType by remember { mutableStateOf("Cap") }
            var medicine by remember { mutableStateOf("Amoxicillin 500") }
            //for bottom action row

            var dose by remember { mutableStateOf("0+0+1") }
            var days by remember { mutableStateOf("৭ দিন") }
            var mealTime by remember { mutableStateOf(MealTime.PORE) }

            DoseAndDrugDropdownRow(
                leftValue = doseType,
                leftItems = listOf("Cap", "Tab", "Syrup"),
                onLeftSelect = { doseType = it },

                rightValue = medicine,
                rightItems = listOf(
                    "Amoxicillin 500",
                    "Azithromycin 250",
                    "Paracetamol 500"
                ),
                onRightSelect = { medicine = it }
            )

            Spacer(Modifier.height(10.dp))

            // Brand name field
            var brand by remember { mutableStateOf("") }
            FormTextField(
                value = brand,
                onValueChange = { brand = it },
                placeholder = "Type Brand Name"
            )

            Spacer(Modifier.height(10.dp))

            // Row 2: dose + duration + timing + icons + tick button
            PrescriptionActionRowAligned(
                doseValue = dose,
                doseItems = listOf("1+0+1", "0+0+1", "1+1+1"),
                onDoseSelect = { dose = it },

                daysValue = days,
                daysItems = listOf("৩ দিন", "৫ দিন", "৭ দিন", "১০ দিন"),
                onDaysSelect = { days = it },

                toggleValue = mealTime,
                onToggleChange = { mealTime = it },

                onMessageClick = {
                    // open note / message
                },
                onAddClick = {
                    // add to list
                }
            )
        }
    }
}

