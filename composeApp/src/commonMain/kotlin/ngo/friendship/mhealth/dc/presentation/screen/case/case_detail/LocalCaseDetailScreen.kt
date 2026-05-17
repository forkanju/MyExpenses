package ngo.friendship.mhealth.dc.presentation.screens.case.case_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ngo.friendship.mhealth.dc.domain.model.SetupData
import ngo.friendship.mhealth.dc.presentation.components.CommonTopBar
import ngo.friendship.mhealth.dc.presentation.components.FormAutoCompleteDropdownField
import ngo.friendship.mhealth.dc.presentation.components.FormContainerCard
import ngo.friendship.mhealth.dc.presentation.components.LabeledFormTextField
import ngo.friendship.mhealth.dc.presentation.screen.case.CaseIntent
import ngo.friendship.mhealth.dc.presentation.screen.case.CaseUiState
import ngo.friendship.mhealth.dc.presentation.screen.case.case_detail.AppDatePickerDialog
import ngo.friendship.mhealth.dc.presentation.screen.case.case_detail.components.DiagnosisChipGroup
import ngo.friendship.mhealth.dc.presentation.screen.case.case_detail.components.InvestigationChipGroup
import ngo.friendship.mhealth.dc.presentation.screen.case.case_detail.components.MedicineSection
import ngo.friendship.mhealth.dc.presentation.screen.case.case_detail.components.PrescriptionActionButtonRow
import ngo.friendship.mhealth.dc.presentation.screen.case.case_detail.components.PrescriptionHeader
import ngo.friendship.mhealth.dc.theme.PrimaryBlue

@Composable
fun LocalCaseDetailScreen(
    state: CaseUiState,
    setupData: SetupData,
    onIntent: (CaseIntent) -> Unit,
    onBack: () -> Unit,
    onSave: () -> Unit,
    onMoreClick: () -> Unit = {}
) {
    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = {
            CommonTopBar(
                title = "New",
                onBack = onBack
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Top Section: Patient Info
            FormContainerCard {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    LabeledFormTextField(
                        label = "Name",
                        placeholder = "Type name",
                        value = state.patientName,
                        onValueChange = { onIntent(CaseIntent.UpdatePatientName(it)) }
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        // Gender Toggle Selection
                        Column(modifier = Modifier.weight(0.35f)) {
                            Text(
                                "Gender",
                                fontSize = 13.sp,
                                color = Color.Gray,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(Modifier.height(4.dp))
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                border = ButtonDefaults.outlinedButtonBorder(enabled = true),
                                color = Color.White,
                                modifier = Modifier.height(44.dp).fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxHeight()
                                            .background(if (state.selectedGender == "Male") PrimaryBlue else Color.Transparent)
                                            .clickable { onIntent(CaseIntent.UpdateGender("Male")) },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            Icons.Default.Male,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp),
                                            tint = if (state.selectedGender == "Male") Color.White else Color.Gray
                                        )
                                    }
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxHeight()
                                            .background(if (state.selectedGender == "Female") PrimaryBlue else Color.Transparent)
                                            .clickable { onIntent(CaseIntent.UpdateGender("Female")) },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            Icons.Default.Female,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp),
                                            tint = if (state.selectedGender == "Female") Color.White else Color.Gray
                                        )
                                    }
                                }
                            }
                        }

                        LabeledFormTextField(
                            modifier = Modifier.weight(0.325f),
                            label = "Age",
                            placeholder = "00",
                            value = state.age,
                            onValueChange = { onIntent(CaseIntent.UpdateAge(it)) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next)
                        )

                        LabeledFormTextField(
                            modifier = Modifier.weight(0.325f),
                            label = "Office ID",
                            placeholder = "0000",
                            value = state.officeId,
                            onValueChange = { onIntent(CaseIntent.UpdateOfficeId(it)) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Sector Dropdown (Placeholder)
                        Column(modifier = Modifier.weight(0.5f)) {
                            Text(
                                "Sector",
                                fontSize = 13.sp,
                                color = Color.Gray,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(40.dp)
                                    .background(Color.White, RoundedCornerShape(4.dp))
                                    .padding(horizontal = 8.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Text(state.sector, fontSize = 14.sp)
                            }
                        }

                        LabeledFormTextField(
                            modifier = Modifier.weight(0.5f),
                            label = "Mobile",
                            placeholder = "01xxxxxxxxx",
                            value = state.formState.mobile,
                            onValueChange = {
                                onIntent(
                                    CaseIntent.UpdateFormState(
                                        state.formState.copy(
                                            mobile = it
                                        )
                                    )
                                )
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next)
                        )
                    }
                }
            }

            // Interview Section
            FormContainerCard {
                Column {
                    Text(
                        "Interview",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryBlue
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = state.interviewNote,
                        onValueChange = { onIntent(CaseIntent.UpdateInterviewNote(it)) },
                        modifier = Modifier.fillMaxWidth().height(150.dp),
                        placeholder = { Text("Type here...") },
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.LightGray,
                            focusedBorderColor = PrimaryBlue
                        )
                    )
                }
            }

            // Prescription Section
            FormContainerCard {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    PrescriptionHeader(
                        isAnsweredMode = false,
                        onMoreClick = onMoreClick
                    )
                    HorizontalDivider()

                    FormAutoCompleteDropdownField(
                        label = "DX",
                        placeholder = "Type",
                        options = setupData.diagnoses,
                        selected = null,
                        getLabel = { it.diagName },
                        onSelectedChange = { selected ->
                            onIntent(CaseIntent.AddDiagnosis(selected))
                        }
                    )

                    if (state.formState.selectedDiagnoses.isNotEmpty()) {
                        DiagnosisChipGroup(
                            items = state.formState.selectedDiagnoses,
                            onRemove = { item ->
                                onIntent(CaseIntent.RemoveDiagnosis(item))
                            }
                        )
                    }

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
                        isAnsweredMode = false,
                        composerState = state.medicineComposerState,
                        onComposerStateChange = { composerState ->
                            onIntent(CaseIntent.UpdateMedicineComposerState(composerState))
                        }
                    )

                    LabeledFormTextField(
                        label = "Doctor advice",
                        placeholder = "Select advice",
                        value = state.formState.doctorAdvice,
                        onValueChange = { onIntent(CaseIntent.UpdateDoctorAdvice(it)) }
                    )

                    FormAutoCompleteDropdownField(
                        label = "Investigations",
                        placeholder = "Select",
                        options = setupData.investigations,
                        selected = null,
                        getLabel = { it.investigationName },
                        onSelectedChange = { selected ->
                            onIntent(CaseIntent.AddInvestigation(selected))
                        }
                    )

                    if (state.formState.selectedInvestigations.isNotEmpty()) {
                        InvestigationChipGroup(
                            items = state.formState.selectedInvestigations,
                            onRemove = { item ->
                                onIntent(CaseIntent.RemoveInvestigation(item))
                            }
                        )
                    }

                    LabeledFormTextField(
                        label = "Doctor notes",
                        placeholder = "Type",
                        value = state.formState.doctorNotes,
                        onValueChange = { onIntent(CaseIntent.UpdateDoctorNotes(it)) }
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 8.dp)
                        ) {
                            Text(
                                text = if (state.formState.nextFollowUpDate.isBlank()) "Next follow-up: " else "Next follow-up: ${state.formState.nextFollowUpDate}",
                                fontSize = 14.sp,
                                color = PrimaryBlue,
                                fontWeight = FontWeight.Bold
                            )
                            IconButton(onClick = { onIntent(CaseIntent.ToggleDatePicker) }) {
                                Icon(
                                    Icons.Default.DateRange,
                                    contentDescription = null,
                                    tint = PrimaryBlue,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    PrescriptionActionButtonRow(
                        onSendClick = onSave,
                        onShareClick = { /* Share */ },
                        sendButtonText = "Save"
                    )
                }
            }
        }

        if (state.isDatePickerVisible) {
            AppDatePickerDialog(
                initialDate = state.formState.nextFollowUpDate,
                onDateSelected = { selectedDate ->
                    onIntent(CaseIntent.UpdateFollowUpDate(selectedDate))
                },
                onDismiss = { onIntent(CaseIntent.ToggleDatePicker) }
            )
        }
    }
}
