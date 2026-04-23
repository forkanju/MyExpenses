package ngo.friendship.mhealth.dc.presentation.screens.more

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.clickable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Male
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ngo.friendship.mhealth.dc.domain.model.Medicine
import ngo.friendship.mhealth.dc.domain.model.SetupData
import ngo.friendship.mhealth.dc.presentation.components.CommonTopBar
import ngo.friendship.mhealth.dc.presentation.components.FormAutoCompleteDropdownField
import ngo.friendship.mhealth.dc.presentation.components.FormContainerCard
import ngo.friendship.mhealth.dc.presentation.components.LabeledFormTextField
import ngo.friendship.mhealth.dc.presentation.screens.case.prescription_form.AppDatePickerDialog
import ngo.friendship.mhealth.dc.presentation.screens.case.prescription_form.components.*
import ngo.friendship.mhealth.dc.presentation.screens.case.prescription_form.model.DoctorFeedbackFormState
import ngo.friendship.mhealth.dc.theme.PrimaryBlue
import ngo.friendship.mhealth.dc.utils.minusAt

@Composable
fun LocalPrescriptionFormScreen(
    onBack: () -> Unit,
    setupData: SetupData,
    medicineList: List<Medicine>,
    onSave: (DoctorFeedbackFormState) -> Unit
) {
    var formState by remember { mutableStateOf(DoctorFeedbackFormState()) }
    var patientName by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("Male") }
    var age by remember { mutableStateOf("") }
    var officeId by remember { mutableStateOf("") }
    var sector by remember { mutableStateOf("ISM") }
    var mobile by remember { mutableStateOf("") }
    var interviewNote by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }

    Scaffold(
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
                        value = patientName,
                        onValueChange = { patientName = it }
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        // Gender Toggle Selection
                        Column(modifier = Modifier.weight(0.35f)) {
                            Text("Gender", fontSize = 13.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
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
                                            .background(if (selectedGender == "Male") PrimaryBlue else Color.Transparent)
                                            .clickable { selectedGender = "Male" },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            Icons.Default.Male,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp),
                                            tint = if (selectedGender == "Male") Color.White else Color.Gray
                                        )
                                    }
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxHeight()
                                            .background(if (selectedGender == "Female") PrimaryBlue else Color.Transparent)
                                            .clickable { selectedGender = "Female" },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            Icons.Default.Female,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp),
                                            tint = if (selectedGender == "Female") Color.White else Color.Gray
                                        )
                                    }
                                }
                            }
                        }

                        LabeledFormTextField(
                            modifier = Modifier.weight(0.325f),
                            label = "Age",
                            placeholder = "00",
                            value = age,
                            onValueChange = { age = it },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )

                        LabeledFormTextField(
                            modifier = Modifier.weight(0.325f),
                            label = "Office ID",
                            placeholder = "0000",
                            value = officeId,
                            onValueChange = { officeId = it },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Sector Dropdown (Placeholder)
                        Column(modifier = Modifier.weight(0.5f)) {
                            Text("Sector", fontSize = 13.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
                            Spacer(Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(40.dp)
                                    .background(Color.White, RoundedCornerShape(4.dp))
                                    .padding(horizontal = 8.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Text(sector, fontSize = 14.sp)
                            }
                        }

                        LabeledFormTextField(
                            modifier = Modifier.weight(0.5f),
                            label = "Mobile",
                            placeholder = "01xxxxxxxxx",
                            value = mobile,
                            onValueChange = { mobile = it },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                        )
                    }
                }
            }

            // Interview Section
            FormContainerCard {
                Column {
                    Text("Interview", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = interviewNote,
                        onValueChange = { interviewNote = it },
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

            // Prescription Section (Reusing components from PrescriptionFormScreen)
            FormContainerCard {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    PrescriptionHeader(isAnsweredMode = false)
                    HorizontalDivider()

                    FormAutoCompleteDropdownField(
                        label = "DX",
                        placeholder = "Type",
                        options = setupData.diagnoses,
                        selected = null,
                        getLabel = { it.diagName },
                        onSelectedChange = { selected ->
                            formState = addDiagnosis(formState, selected)
                        }
                    )

                    if (formState.selectedDiagnoses.isNotEmpty()) {
                        DiagnosisChipGroup(
                            items = formState.selectedDiagnoses,
                            onRemove = { item ->
                                formState = removeDiagnosis(formState, item)
                            }
                        )
                    }

                    MedicineSection(
                        medicines = medicineList,
                        prescriptionItems = formState.prescriptions,
                        onAddMedicine = { item ->
                            formState = formState.copy(prescriptions = formState.prescriptions + item)
                        },
                        onRemoveMedicine = { index ->
                            formState = formState.copy(prescriptions = formState.prescriptions.minusAt(index))
                        },
                        isAnsweredMode = false
                    )

                    LabeledFormTextField(
                        label = "Doctor advice",
                        placeholder = "Select advice",
                        value = formState.doctorAdvice,
                        onValueChange = { formState = formState.copy(doctorAdvice = it) }
                    )

                    FormAutoCompleteDropdownField(
                        label = "Investigations",
                        placeholder = "Select",
                        options = setupData.investigations,
                        selected = null,
                        getLabel = { it.investigationName },
                        onSelectedChange = { selected ->
                            formState = addInvestigation(formState, selected)
                        }
                    )

                    if (formState.selectedInvestigations.isNotEmpty()) {
                        InvestigationChipGroup(
                            items = formState.selectedInvestigations,
                            onRemove = { item ->
                                formState = removeInvestigation(formState, item)
                            }
                        )
                    }

                    LabeledFormTextField(
                        label = "Doctor notes",
                        placeholder = "Type",
                        value = formState.doctorNotes,
                        onValueChange = { formState = formState.copy(doctorNotes = it) }
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
                                text = if (formState.nextFollowUpDate.isBlank()) "Next follow-up: " else "Next follow-up: ${formState.nextFollowUpDate}",
                                fontSize = 14.sp,
                                color = PrimaryBlue,
                                fontWeight = FontWeight.Bold
                            )
                            IconButton(onClick = { showDatePicker = true }) {
                                Icon(Icons.Default.DateRange, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(20.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    PrescriptionActionButtonRow(
                        onSendClick = { onSave(formState) },
                        onShareClick = { /* Share */ },
                        sendButtonText = "Save"
                    )
                }
            }
        }

        if (showDatePicker) {
            AppDatePickerDialog(
                initialDate = formState.nextFollowUpDate,
                onDateSelected = { selectedDate ->
                    formState = formState.copy(nextFollowUpDate = selectedDate)
                },
                onDismiss = { showDatePicker = false }
            )
        }
    }
}
