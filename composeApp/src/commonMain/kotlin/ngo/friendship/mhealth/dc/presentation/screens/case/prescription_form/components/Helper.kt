package ngo.friendship.mhealth.dc.presentation.screens.case.prescription_form.components

import ngo.friendship.mhealth.dc.domain.model.Diagnosis
import ngo.friendship.mhealth.dc.domain.model.Investigation
import ngo.friendship.mhealth.dc.presentation.screens.case.prescription_form.model.DoctorFeedbackFormState

fun addDiagnosis(
    state: DoctorFeedbackFormState,
    item: Diagnosis
): DoctorFeedbackFormState {
    if (state.selectedDiagnoses.any { it.diagId == item.diagId }) return state
    return state.copy(selectedDiagnoses = state.selectedDiagnoses + item)
}

fun removeDiagnosis(
    state: DoctorFeedbackFormState,
    item: Diagnosis
): DoctorFeedbackFormState {
    return state.copy(
        selectedDiagnoses = state.selectedDiagnoses.filterNot { it.diagId == item.diagId }
    )
}

fun addInvestigation(
    state: DoctorFeedbackFormState,
    item: Investigation
): DoctorFeedbackFormState {
    if (state.selectedInvestigations.any { it.investigationId == item.investigationId }) return state
    return state.copy(selectedInvestigations = state.selectedInvestigations + item)
}

fun removeInvestigation(
    state: DoctorFeedbackFormState,
    item: Investigation
): DoctorFeedbackFormState {
    return state.copy(
        selectedInvestigations = state.selectedInvestigations.filterNot {
            it.investigationId == item.investigationId
        }
    )
}

