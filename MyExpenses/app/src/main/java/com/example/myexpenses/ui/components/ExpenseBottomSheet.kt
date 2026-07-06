package com.example.myexpenses.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.myexpenses.data.Expense
import com.example.myexpenses.data.ExpenseCategory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseBottomSheet(
    expense: Expense? = null,
    onSave: (Expense) -> Unit,
    onDismiss: () -> Unit
) {
    var amount by remember { mutableStateOf(expense?.amount?.toString() ?: "") }
    var description by remember { mutableStateOf(expense?.description ?: "") }
    var category by remember { mutableStateOf(expense?.category ?: ExpenseCategory.OTHERS) }
    var expanded by remember { mutableStateOf(false) }
    
    var selectedDate by remember { mutableLongStateOf(expense?.date ?: System.currentTimeMillis()) }
    var showDatePicker by remember { mutableStateOf(false) }

    var amountError by remember { mutableStateOf(false) }
    var descriptionError by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate
    )

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        selectedDate = it
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .padding(bottom = 32.dp) // Extra padding for navigation bar
                .fillMaxWidth()
                .navigationBarsPadding(), // Handle navigation bar insets
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = if (expense == null) "Add New Expense" else "Edit Expense",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )

            OutlinedTextField(
                value = amount,
                onValueChange = { 
                    amount = it
                    amountError = false
                },
                label = { Text("Amount") },
                isError = amountError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                supportingText = if (amountError) { { Text("Please enter a valid amount") } } else null
            )

            OutlinedTextField(
                value = description,
                onValueChange = { 
                    description = it
                    descriptionError = false
                },
                label = { Text("Description") },
                isError = descriptionError,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                supportingText = if (descriptionError) { { Text("Description cannot be empty") } } else null
            )

            // Date Field
            OutlinedTextField(
                value = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(selectedDate)),
                onValueChange = { },
                readOnly = true,
                label = { Text("Date") },
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.CalendarMonth, contentDescription = "Select Date")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true }
            )

            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = category.name.lowercase().replaceFirstChar { it.uppercase() },
                    onValueChange = { },
                    readOnly = true,
                    label = { Text("Category") },
                    trailingIcon = {
                        IconButton(onClick = { expanded = true }) {
                            Icon(Icons.Default.ArrowDropDown, "Dropdown")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expanded = true }
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    ExpenseCategory.entries.forEach { cat ->
                        DropdownMenuItem(
                            text = { Text(cat.name.lowercase().replaceFirstChar { it.uppercase() }) },
                            onClick = {
                                category = cat
                                expanded = false
                            }
                        )
                    }
                }
            }

            Button(
                onClick = {
                    val amountDouble = amount.toDoubleOrNull() ?: 0.0
                    val isDescriptionValid = description.isNotBlank()
                    val isAmountValid = amountDouble > 0
                    
                    if (isDescriptionValid && isAmountValid) {
                        onSave(
                            Expense(
                                id = expense?.id ?: 0,
                                amount = amountDouble,
                                category = category,
                                description = description,
                                date = selectedDate
                            )
                        )
                        onDismiss()
                    } else {
                        amountError = !isAmountValid
                        descriptionError = !isDescriptionValid
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(if (expense == null) "Save Expense" else "Update Expense")
            }
        }
    }
}
