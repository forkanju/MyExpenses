package ngo.friendship.mhealth.dc.presentation.screen.case.case_detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContactPage
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import ngo.friendship.mhealth.dc.presentation.screen.case.case_detail.model.CustomMessageState

@Composable
fun SendMessageDialog(
    initialState: CustomMessageState,
    onDismiss: () -> Unit,
    onUpdateClick: (CustomMessageState) -> Unit,
    modifier: Modifier = Modifier
) {
    var state by remember(initialState) { mutableStateOf(initialState) }

    Dialog(onDismissRequest = onDismiss) {
        CustomMessageDialogContent(
            state = state,
            onMessageChange = { state = state.copy(messageText = it) },
            onFcmToggle = { state = state.copy(isFcmChecked = it) },
            onBeneficiaryToggle = { state = state.copy(isBeneficiaryChecked = it) },
            onPhoneNumberChange = { state = state.copy(phoneNumber = it) },
            onUpdateClick = { onUpdateClick(state) },
            onDismiss = onDismiss,
            modifier = modifier
        )
    }
}

@Composable
fun CustomMessageDialogContent(
    state: CustomMessageState,
    onMessageChange: (String) -> Unit,
    onFcmToggle: (Boolean) -> Unit,
    onBeneficiaryToggle: (Boolean) -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    onUpdateClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Custom messages for sending",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "SMS",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = state.messageText,
                onValueChange = onMessageChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                shape = RoundedCornerShape(8.dp),
                placeholder = {
                    Text("Write message")
                },
                minLines = 5,
                maxLines = 8,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.LightGray
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CheckboxRow(
                    label = "FCM",
                    checked = state.isFcmChecked,
                    onCheckedChange = onFcmToggle
                )

                Spacer(modifier = Modifier.width(16.dp))

                CheckboxRow(
                    label = "Beneficiary",
                    checked = state.isBeneficiaryChecked,
                    onCheckedChange = onBeneficiaryToggle
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = state.phoneNumber,
                onValueChange = onPhoneNumberChange,
                modifier = Modifier.width(180.dp).height(44.dp),
                shape = RoundedCornerShape(8.dp),
                placeholder = {
                    Text(
                        text = "Add Number",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Normal
                        ),
                        color = Color.Gray
                    )
                },
                singleLine = true,
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.ContactPage,
                        contentDescription = "Contacts",
                        tint = Color.Gray
                    )
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {

                Button(
                    onClick = onUpdateClick,
                    modifier = Modifier.fillMaxWidth(.8f).height(42.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF284B96)
                    )
                ) {
                    Text(
                        text = "Update",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun CheckboxRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = Color(0xFF284B96)
            )
        )
        Text(
            text = label,
            color = Color.DarkGray,
            fontWeight = FontWeight.Normal,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}