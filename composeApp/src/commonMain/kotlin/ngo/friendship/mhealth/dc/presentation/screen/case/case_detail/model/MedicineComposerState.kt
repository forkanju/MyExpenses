package ngo.friendship.mhealth.dc.presentation.screen.case.case_detail.model

import androidx.compose.ui.text.input.TextFieldValue
import ngo.friendship.mhealth.dc.presentation.screen.case.case_detail.components.MealTime

data class MedicineComposerState(
    val medicineId: Long = -1L,
    val doseType: String = "",
    val medicineQuery: TextFieldValue = TextFieldValue(""),
    val dose: String = "0+0+1",
    val days: String = "7 days",
    val mealTime: MealTime = MealTime.AFTER,
    val genericNameQuery: TextFieldValue = TextFieldValue("")
)
