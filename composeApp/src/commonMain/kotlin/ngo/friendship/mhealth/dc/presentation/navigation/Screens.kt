package ngo.friendship.mhealth.dc.presentation.navigation

import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import ngo.friendship.mhealth.dc.presentation.screens.case.CaseDetailsMode

@Serializable
object Screens {
    @Serializable
    data object Auth : NavKey

    @Serializable
    data object ForgotPassword : NavKey

    @Serializable
    data object Main : NavKey

    @Serializable
    data class PrescriptionForm(
        val interviewId: Long,
        val mode: CaseDetailsMode = CaseDetailsMode.NORMAL,
        val source: String = SOURCE_CASE_LIST
    ) : NavKey {
        companion object {
            const val SOURCE_CASE_LIST = "case_list"
            const val SOURCE_TEMPLATE_LIST = "template_list"
        }
    }

    @Serializable
    data object PrescriptionTemplateList : NavKey

    @Serializable
    data object DxList : NavKey

    @Serializable
    data object MedicineList : NavKey

    @Serializable
    data object AdviceTemplateList : NavKey

    @Serializable
    data object InvestigationsList : NavKey

    @Serializable
    data object LocalTreatment : NavKey

    @Serializable
    data class LocalTreatmentDetails(val patientId: String) : NavKey


    @Serializable
    object Dialog {
        @Serializable
        data class Error(
            val body: String,
            val title: String = "Warning"
        ) : NavKey

        @Serializable
        data class Confirmation(
            val isConfirmRed: Boolean = false,
            val message: String,
            val title: String = "Confirmation",
            val confirmText: String = "Confirm",
            val cancelText: String = "Cancel",
            val action: ConfirmAction,
        ) : NavKey

        @Serializable
        data object ProfilePopup : NavKey
    }

}

enum class ConfirmAction {
    Logout,
    Exit,
}

// Create a SerializersModule to define polymorphism
val navKeySerializersModule = SerializersModule {
    //screen
    polymorphic(NavKey::class, Screens.Auth::class, Screens.Auth.serializer())
    polymorphic(NavKey::class, Screens.ForgotPassword::class, Screens.ForgotPassword.serializer())
    polymorphic(NavKey::class, Screens.Main::class, Screens.Main.serializer())
    polymorphic(
        NavKey::class,
        Screens.PrescriptionForm::class,
        Screens.PrescriptionForm.serializer()
    )
    polymorphic(NavKey::class, Screens.PrescriptionTemplateList::class, Screens.PrescriptionTemplateList.serializer())
    polymorphic(NavKey::class, Screens.DxList::class, Screens.DxList.serializer())
    polymorphic(NavKey::class, Screens.MedicineList::class, Screens.MedicineList.serializer())
    polymorphic(NavKey::class, Screens.AdviceTemplateList::class, Screens.AdviceTemplateList.serializer())
    polymorphic(NavKey::class, Screens.InvestigationsList::class, Screens.InvestigationsList.serializer())
    polymorphic(NavKey::class, Screens.LocalTreatment::class, Screens.LocalTreatment.serializer())
    polymorphic(NavKey::class, Screens.LocalTreatmentDetails::class, Screens.LocalTreatmentDetails.serializer())
    //dialog
    polymorphic(NavKey::class, Screens.Dialog.Error::class, Screens.Dialog.Error.serializer())
    polymorphic(
        NavKey::class,
        Screens.Dialog.Confirmation::class,
        Screens.Dialog.Confirmation.serializer()
    )
    polymorphic(
        NavKey::class,
        Screens.Dialog.ProfilePopup::class,
        Screens.Dialog.ProfilePopup.serializer()
    )
}

val navConfiguration = SavedStateConfiguration {
    serializersModule = navKeySerializersModule
}