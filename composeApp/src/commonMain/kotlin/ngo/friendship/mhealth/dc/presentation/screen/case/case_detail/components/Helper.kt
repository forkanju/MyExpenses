package ngo.friendship.mhealth.dc.presentation.screens.case.case_detail.components

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import ngo.friendship.mhealth.dc.data.remote.dto.PrescriptionItem
import ngo.friendship.mhealth.dc.domain.model.Diagnosis
import ngo.friendship.mhealth.dc.domain.model.InterviewDetails
import ngo.friendship.mhealth.dc.domain.model.Investigation
import ngo.friendship.mhealth.dc.presentation.screen.case.case_detail.model.DoctorFeedbackFormState

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

fun addInvestigation2(
    state: DoctorFeedbackFormState,
    item: Investigation
): DoctorFeedbackFormState {
    if (state.selectedInvestigations.any { it.investigationId == item.investigationId }) return state
    return state.copy(
        selectedInvestigations = state.selectedInvestigations + item,
        investigationResult = item.investigationName
    )
}

fun addInvestigation(
    state: DoctorFeedbackFormState,
    item: Investigation
): DoctorFeedbackFormState {
    if (state.selectedInvestigations.any { it.investigationId == item.investigationId }) return state

    return state.copy(
        selectedInvestigations = state.selectedInvestigations + item
    )
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


fun String.toEpochMillisOrNull(): Long? {
    return runCatching {
        val localDate = LocalDate.parse(this) // expects yyyy-MM-dd
        localDate.atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    }.getOrNull()
}

fun Long.toDateString(): String {
    return Instant.fromEpochMilliseconds(this)
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .date
        .toString() // yyyy-MM-dd
}

fun buildDefaultSmsMessage(
    interviewDetails: InterviewDetails,
    prescriptions: List<PrescriptionItem>
): String {

    val headerLine = listOfNotNull(
        interviewDetails.beneficiaryName.takeIf { it.isNotBlank() },
        interviewDetails.beneficiaryCode.takeIf { it.isNotBlank() },
        interviewDetails.beneficiaryAge.takeIf { it?.isNotBlank() ?: false },
    ).joinToString(", ")

    val medicineLines = prescriptions.mapIndexedNotNull { index, item ->
        val medicineName = item.medicineName.trim()
        val dose = item.dose.trim()
        val duration = item.duration.trim()

        if (medicineName.isBlank()) return@mapIndexedNotNull null

        buildString {
            append("${index + 1}. ")
            append(medicineName)

            if (dose.isNotBlank()) {
                append(", ")
                append(dose)
            }

            if (duration.isNotBlank()) {
                append(", ")
                append(duration)
            }
        }
    }

    return buildString {
        appendLine("Rx")
        if (headerLine.isNotBlank()) {
            appendLine(headerLine)
        }
        medicineLines.forEach { appendLine(it) }
    }.trim()
}

