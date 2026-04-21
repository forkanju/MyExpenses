package ngo.friendship.mhealth.dc.presentation.screens.more.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ngo.friendship.mhealth.dc.presentation.components.FormDropdownField
import ngo.friendship.mhealth.dc.presentation.components.LabeledFormTextField
import ngo.friendship.mhealth.dc.theme.FriendshipTheme
import ngo.friendship.mhealth.dc.theme.PrimaryBlue

@Composable
fun NewMedicineDialog(
    onDismiss: () -> Unit,
    onCreate: (String, String, String, String) -> Unit
) {
    var selectedItemType by remember { mutableStateOf<String?>(null) }
    var genericName by remember { mutableStateOf("") }
    var medicineStrength by remember { mutableStateOf("") }
    var selectedBrands by remember { mutableStateOf("") }

    val itemTypes = listOf("Tab", "Capsule", "Syrup", "Injection")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.35f))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onDismiss()
            },
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .wrapContentHeight()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    // Prevent dismissal when clicking inside the dialog
                },
            shape = RoundedCornerShape(4.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Text(
                    text = "New Medicine",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(16.dp)
                )

                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))

                Column(modifier = Modifier.padding(16.dp)) {
                    FormDropdownField(
                        label = "Select Item",
                        placeholder = "Select",
                        options = itemTypes,
                        selected = selectedItemType,
                        getLabel = { it },
                        onSelectedChange = { selectedItemType = it }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    LabeledFormTextField(
                        label = "Generic name",
                        placeholder = "Type",
                        value = genericName,
                        onValueChange = { genericName = it }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    LabeledFormTextField(
                        label = "Medicine Strength (use comma \",\" after type single entry)",
                        placeholder = "Type (Example: ml/mg/IU or Others)",
                        value = medicineStrength,
                        onValueChange = { medicineStrength = it }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    LabeledFormTextField(
                        label = "Select Brands (use comma \",\" after type single entry)",
                        placeholder = "Type",
                        value = selectedBrands,
                        onValueChange = { selectedBrands = it }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            onCreate(
                                selectedItemType ?: "",
                                genericName,
                                medicineStrength,
                                selectedBrands
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(text = "Create", color = Color.White)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NewMedicineDialogPreview() {
    FriendshipTheme {
        NewMedicineDialog(
            onDismiss = {},
            onCreate = { _, _, _, _ -> }
        )
    }
}
