package ngo.friendship.mhealth.dc.presentation.screens.case.prescription_form.model

data class MedItem(
    val type: String,          // Cap/Tab/Syrup etc
    val name: String,          // Amoxicillin 500
    val brand: String,         // Type Brand Name
    val dose: String,          // 1+0+1
    val duration: String,      // 7 দিন
    val timing: String         // পরে
)