package ngo.friendship.mhealth.dc.presentation.screens.more.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ngo.friendship.mhealth.dc.presentation.components.LabeledFormTextField
import ngo.friendship.mhealth.dc.theme.FriendshipTheme
import ngo.friendship.mhealth.dc.theme.PrimaryBlue

@Composable
fun CommonNewItemDialog(
    dialogTitle: String,
    titleLabel: String,
    contentLabel: String,
    onDismiss: () -> Unit,
    onCreate: (String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

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
                .fillMaxWidth(0.8f)
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
                    .padding(bottom = 12.dp)
            ) {
                Text(
                    text = dialogTitle,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(10.dp)
                )

                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))

                Column(modifier = Modifier.padding(10.dp)) {
                    LabeledFormTextField(
                        label = titleLabel,
                        placeholder = "",
                        value = title,
                        onValueChange = { title = it }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    LabeledFormTextField(
                        label = contentLabel,
                        placeholder = "Type",
                        value = content,
                        onValueChange = { content = it },
                        height = 200,
                        singleLine = false,
                        maxLines = 15
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { onCreate(title, content) },
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
fun CommonNewItemDialogPreview() {
    FriendshipTheme {
        CommonNewItemDialog(
            dialogTitle = "New DX",
            titleLabel = "DX Title",
            contentLabel = "Advices",
            onDismiss = {},
            onCreate = { _, _ -> }
        )
    }
}
