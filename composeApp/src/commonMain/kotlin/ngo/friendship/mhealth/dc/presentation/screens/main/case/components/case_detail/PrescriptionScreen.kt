package ngo.friendship.mhealth.dc.presentation.screens.main.case.components.case_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.serialization.json.Json
import ngo.friendship.mhealth.dc.data.remote.dto.DoctorFeedbackParam1
import ngo.friendship.mhealth.dc.data.remote.dto.toDto
import ngo.friendship.mhealth.dc.domain.model.InterviewDetails
import ngo.friendship.mhealth.dc.domain.model.Medicine
import ngo.friendship.mhealth.dc.domain.model.SetupData
import ngo.friendship.mhealth.dc.presentation.screens.main.case.SaveDoctorFeedbackViewModel
import ngo.friendship.mhealth.dc.presentation.screens.main.case.save_feedback.DiagnosisChipGroup
import ngo.friendship.mhealth.dc.presentation.screens.main.case.save_feedback.DoctorFeedbackFormState
import ngo.friendship.mhealth.dc.presentation.screens.main.case.save_feedback.InvestigationChipGroup
import ngo.friendship.mhealth.dc.presentation.screens.main.case.save_feedback.addDiagnosis
import ngo.friendship.mhealth.dc.presentation.screens.main.case.save_feedback.addInvestigation
import ngo.friendship.mhealth.dc.presentation.screens.main.case.save_feedback.removeDiagnosis
import ngo.friendship.mhealth.dc.presentation.screens.main.case.save_feedback.removeInvestigation
import ngo.friendship.mhealth.dc.presentation.state.DisplayResult
import ngo.friendship.mhealth.dc.presentation.state.RequestState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrescriptionScreen(
    modifier: Modifier = Modifier,
    interviewDetailsState: RequestState<InterviewDetails>,
    setupData: SetupData = SetupData(),
    medicineListState: RequestState<List<Medicine>>,
    saveDoctorFeedbackViewModel: SaveDoctorFeedbackViewModel,
    onSaveSuccess: () -> Unit = {}
) {
    val title = when (interviewDetailsState) {
        is RequestState.Success -> interviewDetailsState.data.beneficiaryName.ifBlank { "Prescription" }
        else -> "Prescription"
    }
    val snackbarHostState = remember { SnackbarHostState() }
    val saveState by saveDoctorFeedbackViewModel.state.collectAsState()
    var formState by remember {
        mutableStateOf(DoctorFeedbackFormState())
    }
// ✅ PUT HERE (TOP LEVEL — NOT inside onSuccess)
    LaunchedEffect(saveState) {
        when (val state = saveState) {

            is RequestState.Success -> {
                if (state.data.isSuccess) {
                    snackbarHostState.showSnackbar("✅ Saved successfully")
                    saveDoctorFeedbackViewModel.clearState()
                    onSaveSuccess()
                } else {
                    snackbarHostState.showSnackbar("❌ ${state.data.message}")
                    saveDoctorFeedbackViewModel.clearState()
                }

            }

            is RequestState.Error -> {
                snackbarHostState.showSnackbar("❌ Error: ${state.message}")
                saveDoctorFeedbackViewModel.clearState()
            }

            else -> Unit
        }
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState, snackbar = { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = Color(0xFF214695),
                    contentColor = Color.White
                )
            })
        },
        topBar = {
            PrescriptionTopBar(
                titlePrefix = title,
                onBack = { },
                onFcmDetailsClick = { },
                onCall = { },
                onWhatsApp = { }
            )
        },
        bottomBar = {}
    ) { padding ->

        interviewDetailsState.DisplayResult(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            onIdle = {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No data loaded yet")
                }
            },
            onLoading = {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            },
            onError = { message ->
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = message,
                        color = Color.Red
                    )
                }
            },
            onSuccess = { details ->

                var checked by remember { mutableStateOf(false) }
                var selectedTab by remember { mutableStateOf(0) }


                LaunchedEffect(details.interviewId) {
                    formState = formState.copy(
                        interviewId = details.interviewId
                    )
                }

                val interviewQaItems = remember(details.details) {
                    details.details.map {
                        QAItem(
                            question = it.questionName,
                            answer = it.answer
                        )
                    }
                }

                val systemPrescriptionItems = remember {
                    listOf(
                        QAItem("Medicine", "Napa Extra"),
                        QAItem("Dose", "1 + 1 + 1 for 5 days"),
                        QAItem("Advice", "Take rest and drink more water")
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    PatientProfileCard()

                    ExpandableInterviewSummary(
                        modifier = Modifier.fillMaxWidth(),
                        interviewItems = interviewQaItems,
                        prescriptionItems = systemPrescriptionItems,
                        uploadedText = details.startTime,
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
                        MedicineAddScreen(
                            medicines = medicineListState.getSuccessDataOrNull() ?: emptyList(),
                            prescriptionItems = formState.prescriptions,
                            onAddMedicine = { item ->
                                formState = formState.copy(
                                    prescriptions = formState.prescriptions + item
                                )
                            },
                            onRemoveMedicine = { index ->
                                formState = formState.copy(
                                    prescriptions = formState.prescriptions.filterIndexed { i, _ -> i != index }
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
                            leftText = "Next follow-up:",
                            leftIcon = {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = "Calendar",
                                    tint = Color(0xFF214695),
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                            rightText = "Save as a template",
                            onLeftClick = {
                                println("Open date picker")
                                formState = formState.copy(nextFollowUpDate = "2026-04-10")
                                println("Selected next follow-up date: ${formState.nextFollowUpDate}")
                            },
                            onRightClick = {
                                println("Save as template")
                            }
                        )

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
                                        println("Edit clicked")
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
                                        saveDoctorFeedbackViewModel.saveDoctorFeedback(
                                            userName = "sharif.dr",
                                            password = "1234",
                                            formState = formState
                                        )
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
            }
        )
    }
}