package ngo.friendship.mhealth.dc.presentation.screens.case.case_detail.model

import androidx.compose.ui.text.input.TextFieldValue
import ngo.friendship.mhealth.dc.presentation.screens.case.case_detail.components.MealTime

data class MedicineComposerState(
    val doseType: String = "",
    val medicineQuery: TextFieldValue = TextFieldValue(""),
    val dose: String = "0+0+1",
    val days: String = "৭ দিন",
    val mealTime: MealTime = MealTime.PORE,
    val genericNameQuery: TextFieldValue = TextFieldValue("")
)
