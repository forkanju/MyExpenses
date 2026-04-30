package ngo.friendship.mhealth.dc.presentation.screens.case.case_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ngo.friendship.mhealth.dc.data.remote.dto.PrescriptionItem
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
import ngo.friendship.mhealth.dc.presentation.navigation.Screens
import ngo.friendship.mhealth.dc.presentation.screens.case.CaseDetailsMode
import ngo.friendship.mhealth.dc.presentation.screens.case.CaseIntent
import ngo.friendship.mhealth.dc.presentation.screens.case.CaseUiState
import ngo.friendship.mhealth.dc.presentation.screens.case.case_detail.components.*
import ngo.friendship.mhealth.dc.theme.FriendshipTheme


@Composable
fun CaseDetailScreen(
    modifier: Modifier = Modifier,
    state: CaseUiState,
    setupData: SetupData,
    mode: CaseDetailsMode = CaseDetailsMode.NORMAL,
    source: String = Screens.CaseDetail.SOURCE_CASE_LIST,
    onIntent: (CaseIntent) -> Unit = {},
    onFcmDetailsClick: () -> Unit,
    onCall: () -> Unit,
    onWhatsApp: () -> Unit,
    onBack: () -> Unit
) {
    val isAnsweredMode = mode == CaseDetailsMode.ANSWERED
    val isFromTemplate = source == Screens.CaseDetail.SOURCE_TEMPLATE_LIST

    val interviewQaItems = remember(state.interviewDetails.details) {
        state.interviewDetails.details.map {
            QAItem(
                question = it.questionName,
                answer = it.answer
            )
        }
    }

    val systemPrescriptionItems = remember(state.interviewDetails.sysPrescriptionList) {
        state.interviewDetails.sysPrescriptionList.map {
            QAItem("Medicine", it.prescription)
        }
    }

    Scaffold(
        modifier = modifier,
        containerColor = if (isAnsweredMode) Color(0xFFEFEFEF) else Color.White,
        topBar = {
            PrescriptionTopBar(
                titlePrefix = if (isFromTemplate) "Prescription Template" else (state.interviewDetails.fcmInfo?.ifBlank { "Prescription" }
                    ?: ""),
                onFcmDetailsClick = onFcmDetailsClick,
                onCall = onCall,
                onWhatsApp = onWhatsApp,
                onBack = onBack,
                showActions = !isFromTemplate
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(if (isAnsweredMode) Color(0xFFEFEFEF) else Color.White)
                .padding(paddingValues)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (!isFromTemplate) {
                PatientProfileCard(
                    benefName = state.interviewDetails.beneficiaryName,
                    isAnsweredMode = isAnsweredMode
                )

                ExpandableInterviewSummary(
                    modifier = Modifier.fillMaxWidth(),
                    interviewItems = interviewQaItems,
                    prescriptionItems = systemPrescriptionItems,
                    uploadedText = state.interviewDetails.startTime,
                    selectedTab = state.selectedSummaryTab,
                    onTabSelect = { onIntent(CaseIntent.SetSummaryTab(it)) },
                    onCopyClick = {},
                    onSeeFullClick = {},
                    expandedInitially = true,
                    isAnsweredMode = isAnsweredMode
                )
            }

            FormContainerCard(containerColor = if (isAnsweredMode) Color(0xFFF7F7F7) else Color.White) {
                PrescriptionHeader(isAnsweredMode = isAnsweredMode)
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
                        onIntent(CaseIntent.AddDiagnosis(selected))
                    },
                    enabled = !isAnsweredMode
                )

                if (state.formState.selectedDiagnoses.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    DiagnosisChipGroup(
                        items = state.formState.selectedDiagnoses,
                        onRemove = { item ->
                            onIntent(CaseIntent.RemoveDiagnosis(item))
                        }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
                if (isAnsweredMode) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        // Static items for preview/testing purposes
                        PrescriptionItemCard(
                            item = PrescriptionItem(
                                medicineName = "Napa 500mg",
                                dose = "1+0+1",
                                duration = "৫ দিন"
                            ),
                            onRemoveClick = {},
                            isAnsweredMode = true
                        )
                        PrescriptionItemCard(
                            item = PrescriptionItem(
                                medicineName = "Amoxicillin 500mg",
                                dose = "1+1+1",
                                duration = "৭ দিন"
                            ),
                            onRemoveClick = {},
                            isAnsweredMode = true
                        )
                    }
                } else {
                    MedicineSection(
                        medicines = state.medicineList,
                        medicineBrandTypeList = state.medicineBrandTypeList,
                        prescriptionItems = state.formState.prescriptions,
                        onAddMedicine = { item ->
                            onIntent(CaseIntent.AddPrescription(item))
                        },
                        onRemoveMedicine = { index ->
                            onIntent(CaseIntent.RemovePrescription(index))
                        },
                        isAnsweredMode = isAnsweredMode,
                        composerState = state.medicineComposerState,
                        onComposerStateChange = { composerState ->
                            onIntent(CaseIntent.UpdateMedicineComposerState(composerState))
                        }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))


                LabeledFormTextField(
                    label = "Doctor Advice",
                    placeholder = "Advice",
                    value = state.formState.doctorAdvice,
                    onValueChange = {
                        onIntent(CaseIntent.UpdateDoctorAdvice(it))
                    },
                    isError = false,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    enabled = !isAnsweredMode
                )

                Spacer(modifier = Modifier.height(12.dp))

                FormAutoCompleteDropdownField(
                    label = "Investigation",
                    placeholder = "Type",
                    options = setupData.investigations,
                    selected = null,
                    getLabel = { it.investigationName },
                    onSelectedChange = { selected ->
                        onIntent(CaseIntent.AddInvestigation(selected))
                    },
                    enabled = !isAnsweredMode
                )

                if (state.formState.selectedInvestigations.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    InvestigationChipGroup(
                        items = state.formState.selectedInvestigations,
                        onRemove = { item ->
                            onIntent(CaseIntent.RemoveInvestigation(item))
                        }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                LabeledFormTextField(
                    label = "Investigation Result",
                    placeholder = "Result",
                    value = state.formState.investigationResult,
                    onValueChange = {
                        onIntent(CaseIntent.UpdateInvestigationResult(it))
                    },
                    isError = false,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    enabled = !isAnsweredMode
                )
                Spacer(modifier = Modifier.height(12.dp))

                LabeledFormTextField(
                    label = "Comments for FCM",
                    placeholder = "Comment",
                    value = state.formState.commentsForFcm,
                    onValueChange = {
                        onIntent(CaseIntent.UpdateCommentsForFcm(it))
                    },
                    isError = false,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    enabled = !isAnsweredMode
                )

                Spacer(modifier = Modifier.height(12.dp))

                FormDropdownField(
                    label = "Refer to others",
                    placeholder = "Select",
                    options = setupData.referralCenters,
                    selected = state.formState.selectedReferralCenter,
                    getLabel = { it.refCenterName },
                    onSelectedChange = { selected ->
                        onIntent(CaseIntent.UpdateReferralCenter(selected))
                    },
                    enabled = !isAnsweredMode
                )

                Spacer(modifier = Modifier.height(12.dp))

                LabeledFormTextField(
                    label = "Doctor notes",
                    placeholder = "Note",
                    value = state.formState.doctorNotes,
                    onValueChange = {
                        onIntent(CaseIntent.UpdateDoctorNotes(it))
                    },
                    isError = false,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    enabled = !isAnsweredMode
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (!isFromTemplate) {
                    FormActionRow(
                        leftText = if (state.formState.nextFollowUpDate.isBlank()) {
                            "Next follow-up:"
                        } else {
                            "Next follow-up: ${state.formState.nextFollowUpDate}"
                        },
                        leftIcon = {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Calendar",
                                tint = if (isAnsweredMode) Color(0xFF6B6B6B) else Color(0xFF214695),
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        rightText = "",//Save as a template
                        onLeftClick = {
                            onIntent(CaseIntent.ToggleDatePicker)
                        },
                        onRightClick = {
                            println("Save as template")
                        },
                        isAnsweredMode = isAnsweredMode
                    )
                }

                if (state.isDatePickerVisible && !isFromTemplate) {
                    AppDatePickerDialog(
                        initialDate = state.formState.nextFollowUpDate,
                        onDateSelected = { selectedDate ->
                            onIntent(CaseIntent.UpdateFollowUpDate(selectedDate))
                        },
                        onDismiss = {
                            onIntent(CaseIntent.ToggleDatePicker)
                        }
                    )
                }

                if (!isFromTemplate) {
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
                                checked = state.isPrescriptionWithSmsChecked,
                                onCheckedChange = { onIntent(CaseIntent.TogglePrescriptionSms(it)) },
                                onEditClick = {
                                    onIntent(
                                        CaseIntent.UpdateCustomMessage(
                                            state.customMessageState.copy(
                                                messageText = buildDefaultSmsMessage(
                                                    interviewDetails = state.interviewDetails,
                                                    prescriptions = state.formState.prescriptions
                                                )
                                            )
                                        )
                                    )
                                    onIntent(CaseIntent.ToggleSendMessageDialog)
                                },
                                enabled = !isAnsweredMode,
                                isAnsweredMode = isAnsweredMode
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            PrescriptionActionButtonRow(
                                onSendClick = { onIntent(CaseIntent.SaveDoctorFeedback) },
                                onShareClick = {
                                    println("Share prescription")
                                },
                                enabled = !isAnsweredMode,
                                isAnsweredMode = isAnsweredMode
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                } else {
                    Spacer(modifier = Modifier.height(24.dp))
                    PrescriptionActionButtonRow(
                        onSendClick = { onIntent(CaseIntent.SaveDoctorFeedback) },
                        onShareClick = {
                            println("Share template")
                        },
                        sendButtonText = "Save Template",
                        enabled = !isAnsweredMode,
                        isAnsweredMode = isAnsweredMode
                    )
                }
            }
        }

        if (state.isSendMessageDialogVisible) {
            SendMessageDialog(
                initialState = state.customMessageState,
                onDismiss = { onIntent(CaseIntent.ToggleSendMessageDialog) },
                onUpdateClick = { updatedState ->
                    onIntent(CaseIntent.UpdateCustomMessage(updatedState))
                    onIntent(CaseIntent.ToggleSendMessageDialog)
                }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CaseDetailScreenPrev() {
    FriendshipTheme {
        CaseDetailScreen(
            state = CaseUiState(
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
                )
            ),
            setupData = SetupData(
                diagnoses = listOf(),
                investigations = listOf(),
                referralCenters = listOf()
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
