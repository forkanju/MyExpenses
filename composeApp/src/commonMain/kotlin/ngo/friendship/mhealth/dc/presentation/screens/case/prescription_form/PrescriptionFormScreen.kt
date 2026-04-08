package ngo.friendship.mhealth.dc.presentation.screens.case.prescription_form

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.Json
import ngo.friendship.mhealth.dc.data.remote.dto.DoctorFeedbackParam1
import ngo.friendship.mhealth.dc.data.remote.dto.PrescriptionItem
import ngo.friendship.mhealth.dc.data.remote.dto.toDto
import ngo.friendship.mhealth.dc.domain.model.InterviewAnswer
import ngo.friendship.mhealth.dc.domain.model.InterviewDetails
import ngo.friendship.mhealth.dc.domain.model.Medicine
import ngo.friendship.mhealth.dc.domain.model.SetupData
import ngo.friendship.mhealth.dc.presentation.components.CheckboxWithEditableText
import ngo.friendship.mhealth.dc.presentation.components.ExpandableInterviewSummary
import ngo.friendship.mhealth.dc.presentation.components.FormActionRow
import ngo.friendship.mhealth.dc.presentation.components.FormAutoCompleteDropdownField
import ngo.friendship.mhealth.dc.presentation.components.FormContainerCard
import ngo.friendship.mhealth.dc.presentation.components.FormDropdownField
import ngo.friendship.mhealth.dc.presentation.components.LabeledFormTextField
import ngo.friendship.mhealth.dc.presentation.components.QAItem
import ngo.friendship.mhealth.dc.presentation.screens.case.prescription_form.components.MedicineSection
import ngo.friendship.mhealth.dc.presentation.screens.case.prescription_form.components.PatientProfileCard
import ngo.friendship.mhealth.dc.presentation.screens.case.prescription_form.components.PrescriptionActionButtonRow
import ngo.friendship.mhealth.dc.presentation.screens.case.prescription_form.components.PrescriptionHeader
import ngo.friendship.mhealth.dc.presentation.screens.case.prescription_form.components.PrescriptionTopBar
import ngo.friendship.mhealth.dc.presentation.screens.case.prescription_form.components.DiagnosisChipGroup
import ngo.friendship.mhealth.dc.presentation.screens.case.prescription_form.model.DoctorFeedbackFormState
import ngo.friendship.mhealth.dc.presentation.screens.case.prescription_form.components.InvestigationChipGroup
import ngo.friendship.mhealth.dc.presentation.screens.case.prescription_form.components.SendMessageDialog
import ngo.friendship.mhealth.dc.presentation.screens.case.prescription_form.components.addDiagnosis
import ngo.friendship.mhealth.dc.presentation.screens.case.prescription_form.components.addInvestigation
import ngo.friendship.mhealth.dc.presentation.screens.case.prescription_form.components.buildDefaultSmsMessage
import ngo.friendship.mhealth.dc.presentation.screens.case.prescription_form.components.removeDiagnosis
import ngo.friendship.mhealth.dc.presentation.screens.case.prescription_form.components.removeInvestigation
import ngo.friendship.mhealth.dc.presentation.screens.case.prescription_form.components.toDateString
import ngo.friendship.mhealth.dc.presentation.screens.case.prescription_form.components.toEpochMillisOrNull
import ngo.friendship.mhealth.dc.presentation.screens.case.prescription_form.model.CustomMessageState
import ngo.friendship.mhealth.dc.theme.FriendshipTheme
import ngo.friendship.mhealth.dc.utils.minusAt
import kotlin.time.Instant


@Composable
fun PrescriptionFormScreen(
    modifier: Modifier = Modifier,
    setupData: SetupData,
    interviewDetails: InterviewDetails,
    medicineList: List<Medicine>,
    onSave: (DoctorFeedbackFormState) -> Unit = {},
    onFcmDetailsClick: () -> Unit,
    onCall: () -> Unit,
    onWhatsApp: () -> Unit,
    onBack: () -> Unit
) {
    var formState by remember {
        mutableStateOf(DoctorFeedbackFormState(interviewId = interviewDetails.interviewId))
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            PrescriptionTopBar(
                titlePrefix = interviewDetails.fcmInfo?.ifBlank { "Prescription" } ?: "",
                onFcmDetailsClick = onFcmDetailsClick,
                onCall = onCall,
                onWhatsApp = onWhatsApp,
                onBack = onBack
            )
        }
    ) { paddingValues ->

        var checked by remember { mutableStateOf(false) }
        var selectedTab by remember { mutableStateOf(0) }
        var showDatePicker by remember { mutableStateOf(false) }
        var showSendMessageDialog by remember { mutableStateOf(false) }
        var customMessageState by remember {
            mutableStateOf(
                CustomMessageState(
                    messageText = "",
                    isFcmChecked = true,
                    isBeneficiaryChecked = true,
                    phoneNumber = ""
                )
            )
        }

        LaunchedEffect(interviewDetails.interviewId) {
            formState = formState.copy(
                interviewId = interviewDetails.interviewId
            )
        }

        val interviewQaItems = remember(interviewDetails.details) {
            interviewDetails.details.map {
                QAItem(
                    question = it.questionName,
                    answer = it.answer
                )
            }
        }

        val systemPrescriptionItems = remember(interviewDetails.sysPrescriptionList) {
            interviewDetails.sysPrescriptionList.map {
                QAItem("Medicine", it.prescription)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PatientProfileCard(benifName = interviewDetails.beneficiaryName)

            ExpandableInterviewSummary(
                modifier = Modifier.fillMaxWidth(),
                interviewItems = interviewQaItems,
                prescriptionItems = systemPrescriptionItems,
                uploadedText = interviewDetails.startTime,
                selectedTab = selectedTab,
                onTabSelect = { selectedTab = it },
                onCopyClick = {},
                onSeeFullClick = {},
                expandedInitially = true
            )

            FormContainerCard {
                PrescriptionHeader()
                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(modifier = Modifier.height(1.dp))
                Spacer(modifier = Modifier.height(12.dp))

                FormAutoCompleteDropdownField(
                    label = "DX",
                    placeholder = "Type",
                    options = setupData.diagnoses,
                    selected = null,//selectedDiagnosis
                    getLabel = { it.diagName },
                    onSelectedChange = { selected ->
                        formState = addDiagnosis(formState, selected)
                    }
                )

                if (formState.selectedDiagnoses.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    DiagnosisChipGroup(
                        items = formState.selectedDiagnoses,
                        onRemove = { item ->
                            formState = removeDiagnosis(formState, item)
                        }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
                MedicineSection(
                    medicines = medicineList,
                    prescriptionItems = formState.prescriptions,
                    onAddMedicine = { item ->
                        formState = formState.copy(
                            prescriptions = formState.prescriptions + item
                        )
                    },
                    onRemoveMedicine = { index ->
                        formState = formState.copy(
                            prescriptions = formState.prescriptions.minusAt(index)
                        )
                    }
                )

                LabeledFormTextField(
                    label = "Doctor Advice",
                    placeholder = "Advice",
                    value = formState.doctorAdvice,
                    onValueChange = {
                        formState = formState.copy(doctorAdvice = it)
                    },
                    isError = false,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )

                Spacer(modifier = Modifier.height(12.dp))

                FormAutoCompleteDropdownField(
                    label = "Investigation",
                    placeholder = "Type",
                    options = setupData.investigations,
                    selected = null,
                    getLabel = { it.investigationName },
                    onSelectedChange = { selected ->
                        println("TTTTTs selected investigation $selected")
                        formState = addInvestigation(formState, selected)
                    }
                )

                if (formState.selectedInvestigations.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    InvestigationChipGroup(
                        items = formState.selectedInvestigations,
                        onRemove = { item ->
                            formState = removeInvestigation(formState, item)
                        }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                LabeledFormTextField(
                    label = "Comments for FCM",
                    placeholder = "Comment",
                    value = formState.commentsForFcm,
                    onValueChange = {
                        formState = formState.copy(commentsForFcm = it)
                    },
                    isError = false,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )

                Spacer(modifier = Modifier.height(12.dp))

                FormDropdownField(
                    label = "Refer to others",
                    placeholder = "Select",
                    options = setupData.referralCenters,
                    selected = formState.selectedReferralCenter,
                    getLabel = { it.refCenterName },
                    onSelectedChange = { selected ->
                        formState = formState.copy(selectedReferralCenter = selected)
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                LabeledFormTextField(
                    label = "Doctor notes",
                    placeholder = "Note",
                    value = formState.doctorNotes,
                    onValueChange = {
                        formState = formState.copy(doctorNotes = it)
                    },
                    isError = false,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )

                Spacer(modifier = Modifier.height(12.dp))

                FormActionRow(
                    leftText = if (formState.nextFollowUpDate.isBlank()) {
                        "Next follow-up:"
                    } else {
                        "Next follow-up: ${formState.nextFollowUpDate}"
                    },
                    leftIcon = {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Calendar",
                            tint = Color(0xFF214695),
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    rightText = "",//Save as a template
                    onLeftClick = {
                        showDatePicker = true
                        println("Open date picker")
//                        formState = formState.copy(nextFollowUpDate = "2026-04-10")
                        println("Selected next follow-up date: ${formState.nextFollowUpDate}")
                    },
                    onRightClick = {
                        println("Save as template")
                    }
                )

                if (showDatePicker) {
                    AppDatePickerDialog(
                        initialDate = formState.nextFollowUpDate,
                        onDateSelected = { selectedDate ->
                            formState = formState.copy(nextFollowUpDate = selectedDate)
                            println("Selected next follow-up date: ${formState.nextFollowUpDate}")
                        },
                        onDismiss = {
                            showDatePicker = false
                        }
                    )
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Column(
                        modifier = Modifier.wrapContentWidth(),
                        horizontalAlignment = Alignment.Start
                    ) {
                        CheckboxWithEditableText(
                            text = "Prescription with SMS",
                            checked = checked,
                            onCheckedChange = { checked = it },
                            onEditClick = {
                                customMessageState = customMessageState.copy(
                                    messageText = buildDefaultSmsMessage
                                        (
                                        interviewDetails = interviewDetails,
                                        prescriptions = formState.prescriptions
                                    )
                                )
                                showSendMessageDialog = true
                            }
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        PrescriptionActionButtonRow(
                            onSendClick = {
                                val param1 = DoctorFeedbackParam1(
                                    doctorFeedbackObject = formState.toDto()
                                )
                                val jsonString = Json.encodeToString(param1)

                                println("PARAM1_JSON: $jsonString")
                                onSave(formState)
                            },
                            onShareClick = {
                                println("Share prescription")
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }

        if (showSendMessageDialog) {
            SendMessageDialog(
                initialState = customMessageState,
                onDismiss = { showSendMessageDialog = false },
                onUpdateClick = { updatedState ->
                    customMessageState = updatedState
                    showSendMessageDialog = false

                    println("updatedState = $updatedState")
                }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PrescriptionFormScreenPrev() {
    FriendshipTheme {
        PrescriptionFormScreen(
            setupData = SetupData(
                diagnoses = listOf(),
                investigations = listOf(),
                referralCenters = listOf()
            ),
            interviewDetails = InterviewDetails(
                interviewId = 1,
                beneficiaryId = 12345,
                beneficiaryName = "John Doe",
                beneficiaryCode = "B12345",
                location = "123 Main Street, Springfield, IL",
                status = "Completed",
                startTime = "2026-04-03 10:00 AM",
                questionnaireId = 2,
                questionnaireName = "Health Assessment",
                stCaption = "Screening Completed",
                printCaption = "For Prescription Fulfillment",
                userName = "Dr. Smith",
                isNotification = true,
                priority = 1,
                fcmInfo = "fcm_token_1234567890",
                waitingFor = "Lab Results",
                stName = "Routine Checkup",
                description = "Patient is waiting for lab results for further consultation.",
                details = listOf(
                    InterviewAnswer(
                        questionId = 1,
                        questionName = "Do you have any allergies?",
                        answer = "No"
                    ),
                    InterviewAnswer(
                        questionId = 2,
                        questionName = "Are you currently taking any medication?",
                        answer = "Yes, Blood Pressure medication"
                    )
                )
            ),
            medicineList = listOf(
                Medicine(
                    medicineId = 101,
                    genericName = "Paracetamol",
                    brandName = "Tylenol",
                    type = "Pain Reliever",
                    boxSize = 20,
                    unitType = "Tablets"
                ),
                Medicine(
                    medicineId = 102,
                    genericName = "Amlodipine",
                    brandName = "Norvasc",
                    type = "Antihypertensive",
                    boxSize = 30,
                    unitType = "Tablets"
                )
            ),
            onFcmDetailsClick = {},
            onCall = {},
            onWhatsApp = {},
            onBack = {}
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDatePickerDialog(
    initialDate: String? = null, // yyyy-MM-dd
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val initialMillis = initialDate
        ?.takeIf { it.isNotBlank() }
        ?.toEpochMillisOrNull()

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialMillis
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    val selectedMillis = datePickerState.selectedDateMillis
                    if (selectedMillis != null) {
                        onDateSelected(selectedMillis.toDateString())
                    }
                    onDismiss()
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}
