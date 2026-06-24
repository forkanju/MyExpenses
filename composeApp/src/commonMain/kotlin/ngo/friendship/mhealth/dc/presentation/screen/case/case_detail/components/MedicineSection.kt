package ngo.friendship.mhealth.dc.presentation.screen.case.case_detail.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ngo.friendship.mhealth.dc.data.remote.dto.PrescriptionItemDto
import ngo.friendship.mhealth.dc.domain.model.Medicine
import ngo.friendship.mhealth.dc.presentation.base.SnackbarController
import ngo.friendship.mhealth.dc.presentation.base.SnackbarType
import ngo.friendship.mhealth.dc.presentation.components.DoseAndDrugAutoCompleteRow
import ngo.friendship.mhealth.dc.presentation.screen.case.case_detail.model.MedicineComposerState
import ngo.friendship.mhealth.dc.theme.DarkerGray
import ngo.friendship.mhealth.dc.theme.FocusedBorderColor
import ngo.friendship.mhealth.dc.theme.FriendshipTheme
import ngo.friendship.mhealth.dc.theme.Gray
import ngo.friendship.mhealth.dc.theme.UnfocusedBorderColor

enum class MealTime {
    BEFORE,   // Before
    AFTER   // After
}

@Composable
fun MedicineSection(
    medicines: List<Medicine>,
    prescriptionItems: List<PrescriptionItemDto>,
    onAddMedicine: (PrescriptionItemDto) -> Unit,
    onRemoveMedicine: (Int) -> Unit,
    medicineBrandTypeList: List<String> = emptyList(),
    isAnsweredMode: Boolean = false,
    composerState: MedicineComposerState = MedicineComposerState(),
    onComposerStateChange: (MedicineComposerState) -> Unit = {},
    beneficiaryName: String = "",
    beneficiaryCode: String = "",
    beneficiaryAge: String = "",
    doseSuggestions: List<String> = emptyList()
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        if (!isAnsweredMode) {
            MedicineComposerCard(
                medicines = medicines,
                onAddClick = onAddMedicine,
                medicineBrandTypeList = medicineBrandTypeList,
                isAnsweredMode = isAnsweredMode,
                state = composerState,
                onStateChange = onComposerStateChange,
                beneficiaryName = beneficiaryName,
                beneficiaryCode = beneficiaryCode,
                beneficiaryAge = beneficiaryAge,
                doseSuggestions = doseSuggestions
            )
        }

        prescriptionItems.forEachIndexed { index, item ->
            PrescriptionItemCard(
                item = item,
                onRemoveClick = { onRemoveMedicine(index) },
                isAnsweredMode = isAnsweredMode
            )
        }
    }
}

@Composable
fun PrescriptionItemCard(
    item: PrescriptionItemDto,
    onRemoveClick: () -> Unit,
    modifier: Modifier = Modifier,
    isAnsweredMode: Boolean = false
) {
    val colorScheme = MaterialTheme.colorScheme
    val itemBg = if (isAnsweredMode) colorScheme.surfaceVariant else colorScheme.surface
    val itemBorder = if (isAnsweredMode) colorScheme.outlineVariant else colorScheme.outline
    val titleColor = if (isAnsweredMode) colorScheme.onSurfaceVariant else colorScheme.onSurface
    val subColor =
        if (isAnsweredMode) colorScheme.onSurfaceVariant.copy(alpha = 0.8f) else colorScheme.onSurface.copy(
            alpha = 0.7f
        )
    val removeColor = if (isAnsweredMode) colorScheme.inverseOnSurface else colorScheme.error

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, itemBorder),
        color = itemBg
    ) {
        Row(
            modifier = Modifier
                .padding(start = 12.dp, top = 8.dp, bottom = 8.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${item.medType}: ${item.genName} (${item.medName})",
                    color = titleColor,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(2.dp))
                val mealTimeText = if (item.afm.isNotBlank()) " | ${item.afm}" else ""
                val quantityText = if (item.medQty.isNotBlank()) " | Qty: ${item.medQty}" else ""
                Text(
                    text = "Dose: ${item.mtr} | Days: ${item.medDuration}$quantityText$mealTimeText",
                    color = subColor,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (!isAnsweredMode) {
                IconButton(onClick = onRemoveClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Remove",
                        tint = removeColor
                    )
                }
            }
        }
    }
}

@Composable
fun MedicineComposerCard(
    medicines: List<Medicine>,
    onAddClick: (PrescriptionItemDto) -> Unit,
    medicineBrandTypeList: List<String> = emptyList(),
    isAnsweredMode: Boolean = false,
    state: MedicineComposerState = MedicineComposerState(),
    onStateChange: (MedicineComposerState) -> Unit = {},
    beneficiaryName: String = "",
    beneficiaryCode: String = "",
    beneficiaryAge: String = "",
    doseSuggestions: List<String> = emptyList()
) {
    val colorScheme = MaterialTheme.colorScheme
    Surface(
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(
            width = 1.dp,
            color = if (isAnsweredMode) colorScheme.outlineVariant else colorScheme.outline
        ),
        tonalElevation = 0.dp,
        color = if (isAnsweredMode) colorScheme.surfaceVariant else colorScheme.surface
    ) {
        Column(Modifier.padding(12.dp)) {
            LaunchedEffect(medicineBrandTypeList) {
                if (state.doseType.isEmpty() && medicineBrandTypeList.isNotEmpty()) {
                    onStateChange(state.copy(doseType = medicineBrandTypeList.first()))
                }
            }

            val genericNames = remember(medicines) {
                medicines.map { it.genericName }.distinct()
            }

            DoseAndDrugAutoCompleteRow(
                leftValue = state.doseType,
                leftItems = medicineBrandTypeList.ifEmpty { listOf("Cap", "Tab", "Syrup") },
                onLeftSelect = { onStateChange(state.copy(doseType = it)) },
                rightValue = state.genericNameQuery,
                onRightValueChange = { onStateChange(state.copy(genericNameQuery = it)) },
                suggestions = genericNames,
                rightPlaceholder = "Type generic name",
                onSuggestionSelected = { selected ->
                    // ... (same as before)
                    val genericNameText = selected.text.trim()

                    val medicine = medicines.find {
                        it.genericName.trim().equals(genericNameText, ignoreCase = true) &&
                                it.brandName.isNotBlank()

                    } ?: medicines.find {
                        it.genericName.trim().equals(genericNameText, ignoreCase = true)
                    }

                    val brandName = medicine?.brandName ?: ""
                    val medicineId = medicine?.medicineId ?: -1L


                    onStateChange(
                        state.copy(
                            genericNameQuery = selected,
                            medicineQuery = TextFieldValue(
                                brandName,
                                TextRange(brandName.length)
                            ),
                            medicineId = medicineId
                        )
                    )
                },
                isAnsweredMode = isAnsweredMode,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            Spacer(Modifier.height(10.dp))

            MedTextField(
                value = state.medicineQuery,
                onValueChange = { onStateChange(state.copy(medicineQuery = it)) },
                placeholder = "Type medicine name",
                isAnsweredMode = isAnsweredMode,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            Spacer(Modifier.height(10.dp))

            PrescriptionActionRowAligned(
                doseValue = state.dose,
                onDoseChange = { onStateChange(state.copy(dose = it)) },
                daysValue = state.days,
                onDaysChange = { onStateChange(state.copy(days = it)) },
                quantityValue = state.quantity,
                onQuantityChange = { onStateChange(state.copy(quantity = it)) },
                toggleValue = state.mealTime,
                onToggleChange = { onStateChange(state.copy(mealTime = it)) },
                onMessageClick = { },
                doseSuggestions = doseSuggestions,
                showMealTime = state.doseType in listOf("Cap", "Tab", "Syp", "Syrup"),
                noteValue = state.note,
                onNoteChange = { onStateChange(state.copy(note = it)) },
                onAddClick = {
                    val genericName = state.genericNameQuery.text.trim()
                    val brandName = state.medicineQuery.text.trim()
                    val type = state.doseType

                    if (type.isBlank()) {
                        SnackbarController.sendEvent("Please select a medicine type", type = SnackbarType.WARNING)
                    } else if (genericName.isBlank()) {
                        SnackbarController.sendEvent("Please enter a generic name", type = SnackbarType.WARNING)
                    } else if (brandName.isBlank()) {
                        SnackbarController.sendEvent("Please enter a medicine name", type = SnackbarType.WARNING)
                    } else {
                        val mealTimeText = when (state.mealTime) {
                            MealTime.BEFORE -> "Before"
                            MealTime.AFTER -> "After"
                        }

                        val smsSf = "Rx\r\n${beneficiaryName}, ${beneficiaryCode}, ${beneficiaryAge}y\r\n${genericName}. ${brandName} , ${state.days}d"

                        val item = PrescriptionItemDto(
                            medId = state.medicineId.toString(),
                            genName = genericName,
                            medType = type,
                            medName = brandName,
                            medQty = state.quantity,
                            saleQty = state.quantity,
                            medDuration = state.days,
                            mtr = state.dose,
                            mtrLbl = state.dose,
                            mtrSf = state.note,
                            afm = state.note,
                            afmSf = state.note,
                            sf = state.note,
                            smsSf = smsSf
                        )

                        onAddClick(item)

                        onStateChange(
                            state.copy(
                                medicineQuery = TextFieldValue(""),
                                genericNameQuery = TextFieldValue(""),
                                dose = "",
                                days = "",
                                quantity = "",
                                mealTime = MealTime.AFTER,
                                medicineId = -1L,
                                note = "",
                                isNoteVisible = false
                            )
                        )
                    }
                },
                isAnsweredMode = isAnsweredMode
            )
        }
    }
}

@Composable
fun MedTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    isAnsweredMode: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
    keyboardActions: KeyboardActions? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val focusManager = LocalFocusManager.current

    val effectiveKeyboardActions = keyboardActions ?: KeyboardActions(
        onNext = {
            focusManager.moveFocus(androidx.compose.ui.focus.FocusDirection.Down)
        }
    )

    val borderColor = when {
        isAnsweredMode -> Color(0xFFC7C7C7)
        isFocused -> FocusedBorderColor
        else -> UnfocusedBorderColor
    }

    val bgColor = if (isAnsweredMode) Color(0xFFF7F7F7) else Color.White
    val textColor = if (isAnsweredMode) Color(0xFF4F4F4F) else DarkerGray

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .height(32.dp),
        textStyle = TextStyle(color = textColor, fontSize = 13.sp),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        interactionSource = interactionSource,
        keyboardOptions = keyboardOptions,
        keyboardActions = effectiveKeyboardActions,
        decorationBox = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(6.dp))
                    .background(bgColor)
                    .border(BorderStroke(1.dp, borderColor), RoundedCornerShape(6.dp))
                    .padding(horizontal = 12.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (value.text.isEmpty()) {
                    Text(placeholder, color = Gray, fontSize = 11.sp)
                }
                it()
            }
        }
    )
}

@Preview
@Composable
fun MedicineComposerCardPreview() {
    FriendshipTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            var state by remember { mutableStateOf(MedicineComposerState()) }
            MedicineComposerCard(
                medicines = listOf(
                    Medicine(medicineId = 1, genericName = "Paracetamol", brandName = "Napa"),
                    Medicine(medicineId = 2, genericName = "Amoxicillin", brandName = "Moxilin")
                ),
                medicineBrandTypeList = listOf("Cap", "Tab", "Syrup", "Injection"),
                onAddClick = {},
                state = state,
                onStateChange = { state = it }
            )

            PrescriptionItemCard(
                item = PrescriptionItemDto(
                    medId = "",
                    genName = "Paracetamol",
                    medType = "Tab",
                    medName = "Napa 500mg",
                    medQty = "",
                    saleQty = "",
                    medDuration = "5 days",
                    mtr = "1+0+1",
                    mtrLbl = "1+0+1",
                    mtrSf = "1+0+1",
                    afm = "",
                    afmSf = "",
                    sf = "",
                    smsSf = ""
                ),
                onRemoveClick = {}
            )
        }
    }
}