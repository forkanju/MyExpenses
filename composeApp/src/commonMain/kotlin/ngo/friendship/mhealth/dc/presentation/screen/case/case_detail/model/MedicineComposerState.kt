package ngo.friendship.mhealth.dc.presentation.screen.case.case_detail.model

import androidx.compose.ui.text.input.TextFieldValue
import ngo.friendship.mhealth.dc.presentation.screen.case.case_detail.components.MealTime

data class MedicineComposerState(
    val medicineId: Long = -1L,
    val doseType: String = "",
    val medicineQuery: TextFieldValue = TextFieldValue(""),
    val dose: String = "",
    val days: String = "",
    val quantity: String = "",
    val mealTime: MealTime = MealTime.AFTER,
    val genericNameQuery: TextFieldValue = TextFieldValue(""),
    val note: String = "",
    val isNoteVisible: Boolean = false
)
