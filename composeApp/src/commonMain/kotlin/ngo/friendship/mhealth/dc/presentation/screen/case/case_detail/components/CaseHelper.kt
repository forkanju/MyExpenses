package ngo.friendship.mhealth.dc.presentation.screen.case.case_detail.components

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import ngo.friendship.mhealth.dc.data.remote.dto.PrescriptionItem
import ngo.friendship.mhealth.dc.domain.model.Diagnosis
import ngo.friendship.mhealth.dc.domain.model.InterviewDetails
import ngo.friendship.mhealth.dc.domain.model.Investigation
import ngo.friendship.mhealth.dc.presentation.screen.case.case_detail.model.DoctorFeedbackFormState
import kotlin.time.Clock
import kotlin.time.Instant

fun String.calculateAge(): String {
    if (this.isBlank()) return ""
    return try {
        val birthDate = LocalDate.parse(this.take(10)) // expects yyyy-MM-dd
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

        var years = today.year - birthDate.year
        var months = today.month.ordinal - birthDate.month.ordinal
        val days = today.day - birthDate.day

        if (days < 0) {
            months -= 1
        }
        if (months < 0) {
            years -= 1
            months += 12
        }

        when {
            years > 0 -> "$years years"
            months > 0 -> "$months months"
            else -> "${maxOf(0, days)} days"
        }
    } catch (_: Exception) {
        ""
    }
}

fun addDiagnosis(
    state: DoctorFeedbackFormState,
    item: Diagnosis
): DoctorFeedbackFormState {
    val alreadyExists = state.selectedDiagnoses.any {
        if (item.diagId.isNotBlank() && item.diagId != "0") {
            it.diagId == item.diagId
        } else {
            it.diagName.equals(item.diagName, ignoreCase = true)
        }
    }
    if (alreadyExists) return state
    return state.copy(selectedDiagnoses = state.selectedDiagnoses + item)
}

fun removeDiagnosis(
    state: DoctorFeedbackFormState,
    item: Diagnosis
): DoctorFeedbackFormState {
    return state.copy(
        selectedDiagnoses = state.selectedDiagnoses.filterNot {
            if (item.diagId.isNotBlank() && item.diagId != "0") {
                it.diagId == item.diagId
            } else {
                it.diagName.equals(item.diagName, ignoreCase = true)
            }
        }
    )
}

fun addInvestigation(
    state: DoctorFeedbackFormState,
    item: Investigation
): DoctorFeedbackFormState {
    val alreadyExists = state.selectedInvestigations.any {
        if (item.investigationId != 0L) {
            it.investigationId == item.investigationId
        } else {
            it.investigationName.equals(item.investigationName, ignoreCase = true)
        }
    }
    if (alreadyExists) return state
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
            if (item.investigationId != 0L) {
                it.investigationId == item.investigationId
            } else {
                it.investigationName.equals(item.investigationName, ignoreCase = true)
            }
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
        if (headerLine.isNotBlank()) {
            appendLine(headerLine)
        }
        appendLine("Rx")
        medicineLines.forEach { appendLine(it) }
    }.trim()
}

