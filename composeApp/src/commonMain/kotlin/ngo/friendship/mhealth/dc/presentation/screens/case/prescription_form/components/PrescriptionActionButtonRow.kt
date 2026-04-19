package ngo.friendship.mhealth.dc.presentation.screens.case.prescription_form.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val NormalPrimaryButton = Color(0xFF214695)
private val NormalPrimaryButtonAlt = Color(0xFF1976D2)
private val AnsweredPrimaryButton = Color(0xFF9E9E9E)
private val AnsweredPrimaryButtonAlt = Color(0xFFB5B5B5)
private val DisabledAnsweredButton = Color(0xFFD0D0D0)
private val DisabledNormalButton = Color(0xFFB0BEC5)
private val ShareIconNormal = Color(0xFF616161)
private val ShareIconAnswered = Color(0xFF7A7A7A)
private val ShareSurfaceNormal = Color.White
private val ShareSurfaceAnswered = Color(0xFFF3F3F3)

@Composable
fun PrescriptionActionButtonRow(
    onSendClick: () -> Unit,
    onShareClick: () -> Unit,
    sendButtonText: String = "Send Prescription",
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    isAnsweredMode: Boolean = false
) {
    val buttonBg = if (isAnsweredMode) AnsweredPrimaryButtonAlt else NormalPrimaryButton
    val disabledButtonBg = if (isAnsweredMode) DisabledAnsweredButton else DisabledNormalButton
    val shareBg = if (isAnsweredMode) ShareSurfaceAnswered else ShareSurfaceNormal
    val shareTint = if (isAnsweredMode) ShareIconAnswered else ShareIconNormal

    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Button(
            onClick = onSendClick,
            enabled = enabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonBg,
                contentColor = Color.White,
                disabledContainerColor = disabledButtonBg,
                disabledContentColor = Color.White.copy(alpha = 0.7f)
            ),
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier.height(32.dp)
        ) {
            Text(
                text = sendButtonText,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(Modifier.width(8.dp))

        Surface(
            onClick = onShareClick,
            modifier = Modifier.size(36.dp),
            shape = CircleShape,
            color = shareBg,
            shadowElevation = 4.dp,
            tonalElevation = 0.dp
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share",
                    tint = shareTint,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun PrescriptionActionButtonsOutlined(
    onSendClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier,
    sendButtonText: String = "Send Prescription",
    enabled: Boolean = true,
    isAnsweredMode: Boolean = false
) {
    val buttonBg = if (isAnsweredMode) AnsweredPrimaryButton else NormalPrimaryButtonAlt
    val disabledButtonBg = if (isAnsweredMode) DisabledAnsweredButton else DisabledNormalButton
    val outlinedIconColor = if (isAnsweredMode) ShareIconAnswered else ShareIconNormal

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = onSendClick,
            enabled = enabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonBg,
                contentColor = Color.White,
                disabledContainerColor = disabledButtonBg,
                disabledContentColor = Color.White.copy(alpha = 0.7f)
            ),
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier.height(40.dp)
        ) {
            Text(
                text = sendButtonText,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(Modifier.width(8.dp))

        OutlinedIconButton(
            onClick = onShareClick,
            enabled = enabled,
            modifier = Modifier.size(40.dp),
            border = ButtonDefaults.outlinedButtonBorder(enabled).copy(width = 1.dp),
            colors = IconButtonDefaults.outlinedIconButtonColors(
                contentColor = outlinedIconColor
            )
        ) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "Share",
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun PrescriptionActionButtonsExamples() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        PrescriptionActionButtonRow(
            onSendClick = {
                println("Send prescription")
            },
            onShareClick = {
                println("Share prescription")
            }
        )

        HorizontalDivider()

        PrescriptionActionButtonsOutlined(
            onSendClick = {
                println("Send prescription")
            },
            onShareClick = {
                println("Share prescription")
            }
        )

        HorizontalDivider()

        PrescriptionActionButtonRow(
            sendButtonText = "Send Now",
            onSendClick = { },
            onShareClick = { }
        )

        HorizontalDivider()

        PrescriptionActionButtonRow(
            onSendClick = { },
            onShareClick = { },
            enabled = false
        )
    }
}

@Composable
fun PrescriptionBottomBar(
    onSendClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier,
    isAnsweredMode: Boolean = false,
    enabled: Boolean = true
) {
    val buttonBg = if (isAnsweredMode) AnsweredPrimaryButton else NormalPrimaryButtonAlt
    val disabledButtonBg = if (isAnsweredMode) DisabledAnsweredButton else DisabledNormalButton
    val shareTint = if (isAnsweredMode) ShareIconAnswered else ShareIconNormal
    val containerBg = if (isAnsweredMode) Color(0xFFF5F5F5) else Color.White

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = containerBg,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onSendClick,
                enabled = enabled,
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonBg,
                    contentColor = Color.White,
                    disabledContainerColor = disabledButtonBg,
                    disabledContentColor = Color.White.copy(alpha = 0.7f)
                ),
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier.height(48.dp)
            ) {
                Text(
                    text = "Send Prescription",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(Modifier.width(8.dp))

            IconButton(
                onClick = onShareClick,
                modifier = Modifier.size(48.dp),
                enabled = enabled
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share",
                    tint = shareTint,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun PrescriptionActionButtonsPreview() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFFF5F5F5)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom
            ) {
                PrescriptionBottomBar(
                    onSendClick = { },
                    onShareClick = { }
                )
            }
        }
    }
}