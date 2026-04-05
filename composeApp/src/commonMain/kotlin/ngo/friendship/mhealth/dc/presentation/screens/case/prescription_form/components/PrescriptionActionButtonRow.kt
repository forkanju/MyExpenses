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

@Composable
fun PrescriptionActionButtonRow(
    onSendClick: () -> Unit,
    onShareClick: () -> Unit,
    sendButtonText: String = "Send Prescription",
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        // Main Send Button
        Button(
            onClick = onSendClick,
            enabled = enabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF214695),
                contentColor = Color.White
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

        // Share Icon Button with circular white background and elevation
        Surface(
            onClick = onShareClick,
            modifier = Modifier.size(36.dp),
            shape = CircleShape,
            color = Color.White,
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
                    tint = Color(0xFF616161),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

// Alternative version with outlined share button
@Composable
fun PrescriptionActionButtonsOutlined(
    onSendClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier,
    sendButtonText: String = "Send Prescription",
    enabled: Boolean = true
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Main Send Button
        Button(
            onClick = onSendClick,
            enabled = enabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1976D2),
                contentColor = Color.White
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

        // Share Outlined Button
        OutlinedIconButton(
            onClick = onShareClick,
            enabled = enabled,
            modifier = Modifier.size(40.dp),
            border = ButtonDefaults.outlinedButtonBorder(enabled).copy(width = 1.dp),
            colors = IconButtonDefaults.outlinedIconButtonColors(
                contentColor = Color(0xFF616161)
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

// Usage Examples:
@Composable
fun PrescriptionActionButtonsExamples() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Example 1: Basic version
        PrescriptionActionButtonRow(
            onSendClick = {
                println("Send prescription")
            },
            onShareClick = {
                println("Share prescription")
            }
        )

        HorizontalDivider()

        // Example 2: Outlined share button
        PrescriptionActionButtonsOutlined(
            onSendClick = {
                println("Send prescription")
            },
            onShareClick = {
                println("Share prescription")
            }
        )

        HorizontalDivider()

        // Example 3: Custom text
        PrescriptionActionButtonRow(
            sendButtonText = "Send Now",
            onSendClick = { },
            onShareClick = { }
        )

        HorizontalDivider()

        // Example 4: Disabled state
        PrescriptionActionButtonRow(
            onSendClick = { },
            onShareClick = { },
            enabled = false
        )
    }
}

// Bottom bar version (like in the original design)
@Composable
fun PrescriptionBottomBar(
    onSendClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Main Send Button
            Button(
                onClick = onSendClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1976D2),
                    contentColor = Color.White
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

            // Share Icon Button
            IconButton(
                onClick = onShareClick,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share",
                    tint = Color(0xFF616161),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

// Preview
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