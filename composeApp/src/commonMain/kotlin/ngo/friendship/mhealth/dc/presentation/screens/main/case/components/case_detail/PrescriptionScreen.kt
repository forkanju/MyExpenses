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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ngo.friendship.mhealth.dc.domain.model.Diagnosis
import ngo.friendship.mhealth.dc.domain.model.InterviewDetails
import ngo.friendship.mhealth.dc.domain.model.Investigation
import ngo.friendship.mhealth.dc.domain.model.Medicine
import ngo.friendship.mhealth.dc.domain.model.MedicineBrandType
import ngo.friendship.mhealth.dc.domain.model.ReferralCenter
import ngo.friendship.mhealth.dc.domain.model.SetupData
import ngo.friendship.mhealth.dc.presentation.state.DisplayResult
import ngo.friendship.mhealth.dc.presentation.state.RequestState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrescriptionScreen(
    modifier: Modifier = Modifier,
    interviewDetailsState: RequestState<InterviewDetails>,
    setupData: SetupData = SetupData(),
    medicineListState: RequestState<List<Medicine>>
) {
    val title = when (interviewDetailsState) {
        is RequestState.Success -> interviewDetailsState.data.beneficiaryName.ifBlank { "Prescription" }
        else -> "Prescription"
    }

    var selectedDiagnosis by remember { mutableStateOf<Diagnosis?>(null) }
    var selectedInvestigation by remember { mutableStateOf<Investigation?>(null) }
    var selectedReferralCenter by remember { mutableStateOf<ReferralCenter?>(null) }
    var selectedMedicineTypes by remember { mutableStateOf<MedicineBrandType?>(null) }


    Scaffold(
        modifier = modifier,
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

                var dx by remember { mutableStateOf<String?>(null) }
                var checked by remember { mutableStateOf(false) }
                var selectedTab by remember { mutableStateOf(0) }

                val interviewQaItems = remember(details.details) {
                    details.details.map {
                        QAItem(
                            question = it.questionName,
                            answer = it.answer
                        )
                    }
                }

                // Temporary demo items for System Prescription tab
                // Later you can replace this with API data
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

                        PrescriptionTemplateSection(
                            chips = listOf(
                                "Oral ulcer prescription by Abir",
                                "Prescription 2",
                                "Ulcer by me",
                                "Follow up"
                            ),
                            onLinkClick = { },
                            onChipClick = { }
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider(modifier = Modifier.height(1.dp))
                        Spacer(modifier = Modifier.height(12.dp))

                        FormDropdownField(
                            label = "DX",
                            placeholder = "Type",
                            options = setupData.diagnoses,
                            selected = selectedDiagnosis,
                            getLabel = { it.diagName },
                            onSelectedChange = { selectedDiagnosis = it }
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        MedicineAddScreen(medicines = medicineListState.getSuccessDataOrNull()?: emptyList())

                        FormDropdownField(
                            label = "Doctor Advice",
                            placeholder = "Select",
                            options = setupData.medicineBrandTypes,
                            selected = selectedMedicineTypes,
                            getLabel = { it.type },
                            onSelectedChange = { selectedMedicineTypes = it }
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        FormDropdownField(
                            label = "Investigation",
                            placeholder = "Select",
                            options = setupData.investigations,
                            selected = selectedInvestigation,
                            getLabel = { it.investigationName },
                            onSelectedChange = { selectedInvestigation = it }
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        LabeledFormTextField(
                            label = "Comments for FCM",
                            placeholder = "Comment",
                            value = "",
                            onValueChange = { },
                            isError = false,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        FormDropdownField(
                            label = "Refer to others",
                            placeholder = "Select",
                            options = setupData.referralCenters,
                            selected = selectedReferralCenter,
                            getLabel = { it.refCenterName },
                            onSelectedChange = { selectedReferralCenter = it }
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        LabeledFormTextField(
                            label = "Doctor notes",
                            placeholder = "Note",
                            value = "",
                            onValueChange = { },
                            isError = false,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
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
                                        println("Send prescription")
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