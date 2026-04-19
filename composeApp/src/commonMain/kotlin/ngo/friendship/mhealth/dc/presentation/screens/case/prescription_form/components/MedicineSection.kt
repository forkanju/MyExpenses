package ngo.friendship.mhealth.dc.presentation.screens.case.prescription_form.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import ngo.friendship.mhealth.dc.data.remote.dto.PrescriptionItem
import ngo.friendship.mhealth.dc.domain.model.Medicine
import ngo.friendship.mhealth.dc.presentation.components.DoseAndDrugAutoCompleteRow
import ngo.friendship.mhealth.dc.presentation.components.MedAutoCompleteTextField
import ngo.friendship.mhealth.dc.theme.UnfocusedBorderColor

enum class MealTime {
    AGE,   // আগে
    PORE   // পরে
}


@Composable
fun MedicineSection(
    medicines: List<Medicine>,
    prescriptionItems: List<PrescriptionItem>,
    onAddMedicine: (PrescriptionItem) -> Unit,
    onRemoveMedicine: (Int) -> Unit,
    isAnsweredMode: Boolean = false
) {
    val itemBg = if (isAnsweredMode) Color(0xFFF7F7F7) else Color.White
    val itemBorder = if (isAnsweredMode) Color(0xFFC7C7C7) else Color(0xFFCBD5E1)
    val titleColor = if (isAnsweredMode) Color(0xFF4F4F4F) else Color.Black
    val subColor = if (isAnsweredMode) Color(0xFF6A6A6A) else Color.Black
    val removeColor = if (isAnsweredMode) Color(0xFF777777) else Color.Black

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        MedicineComposerCard(
            medicines = medicines,
            onAddClick = onAddMedicine,
            isAnsweredMode = isAnsweredMode
        )

        prescriptionItems.forEachIndexed { index, item ->
            Surface(
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, itemBorder),
                color = itemBg
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = item.medicineName, color = titleColor)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Dose: ${item.dose} | Days: ${item.duration}",
                            color = subColor
                        )
                    }

                    Text(
                        text = "Remove",
                        color = removeColor,
                        modifier = Modifier.clickable { onRemoveMedicine(index) }
                    )
                }
            }
        }
    }
}

@Composable
fun MedicineComposerCard(
    medicines: List<Medicine>,
    onAddClick: (PrescriptionItem) -> Unit,
    isAnsweredMode: Boolean = false
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(
            width = 1.dp,
            color = if (isAnsweredMode) Color(0xFFC7C7C7) else UnfocusedBorderColor
        ),
        tonalElevation = 0.dp,
        color = if (isAnsweredMode) Color(0xFFF7F7F7) else Color.White
    ) {
        Column(Modifier.padding(12.dp)) {
            var doseType by remember { mutableStateOf("Cap") }
            var dropdownMedicine by remember { mutableStateOf("Amoxicillin 500") }
            var medicineQuery by remember { mutableStateOf(TextFieldValue("")) }

            var dose by remember { mutableStateOf("0+0+1") }
            var days by remember { mutableStateOf("৭ দিন") }
            var mealTime by remember { mutableStateOf(MealTime.PORE) }

            val medicineNames = remember(medicines) {
                medicines.map { "" }
            }

            var genericNameQuery by remember { mutableStateOf(TextFieldValue("")) }
            val genericNames = remember(medicines) {
                medicines.map { it.genericName }.distinct()
            }

            DoseAndDrugAutoCompleteRow(
                leftValue = doseType,
                leftItems = listOf("Cap", "Tab", "Syrup"),
                onLeftSelect = { doseType = it },
                rightValue = genericNameQuery,
                onRightValueChange = { genericNameQuery = it },
                suggestions = genericNames,
                rightPlaceholder = "Type generic name",
                onSuggestionSelected = { selected ->
                    genericNameQuery = selected
                },
                isAnsweredMode = isAnsweredMode
            )

            Spacer(Modifier.height(10.dp))

            MedAutoCompleteTextField(
                value = medicineQuery,
                onValueChange = { medicineQuery = it },
                suggestions = medicineNames,
                placeholder = "Type medicine name",
                onSuggestionSelected = { selectedValue ->
                    medicineQuery = TextFieldValue(
                        text = selectedValue.text,
                        selection = TextRange(selectedValue.text.length)
                    )
                },
                isAnsweredMode = isAnsweredMode
            )

            Spacer(Modifier.height(10.dp))

            PrescriptionActionRowAligned(
                doseValue = dose,
                doseItems = listOf("1+0+1", "0+0+1", "1+1+1"),
                onDoseSelect = { dose = it },
                daysValue = days,
                daysItems = listOf("৩ দিন", "৫ দিন", "৭ দিন", "১০ দিন"),
                onDaysSelect = { days = it },
                toggleValue = mealTime,
                onToggleChange = { mealTime = it },
                onMessageClick = { },
                onAddClick = {
                    val finalMedicineName = medicineQuery.text.trim().ifBlank { dropdownMedicine }

                    if (finalMedicineName.isNotBlank()) {
                        val item = PrescriptionItem(
                            medicineName = finalMedicineName,
                            dose = dose,
                            duration = days
                        )

                        onAddClick(item)

                        medicineQuery = TextFieldValue("")
                        dropdownMedicine = "Amoxicillin 500"
                        dose = "0+0+1"
                        days = "৭ দিন"
                        mealTime = MealTime.PORE
                    }
                },
                isAnsweredMode = isAnsweredMode
            )
        }
    }
}