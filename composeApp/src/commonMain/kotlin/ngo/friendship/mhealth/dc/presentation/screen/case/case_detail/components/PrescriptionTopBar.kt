package ngo.friendship.mhealth.dc.presentation.screens.case.case_detail.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ngo.friendship.mhealth.dc.theme.FriendshipTheme
import ngo.friendship.mhealth.dc.theme.PrimaryBlue
import ngo.friendship.mhealth.dc.theme.Resources
import ngo.friendship.mhealth.dc.theme.RobotoCondensedFont
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrescriptionTopBar(
    titlePrefix: String,
    onFcmDetailsClick: () -> Unit,
    onCall: () -> Unit,
    onWhatsApp: () -> Unit,
    onBack: () -> Unit,
    showActions: Boolean = true,
    detailsButtonText: String = "(View)"
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        },
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = titlePrefix,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Normal,
                        fontSize = 18.sp,
                        letterSpacing = 0.sp,
                        color = Color.White
                    )
                )


                if (showActions) {
                    Spacer(Modifier.width(6.dp))

                    Text(
                        text = detailsButtonText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = RobotoCondensedFont(),
                        letterSpacing = 0.sp,
                        color = Color.White,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable(
                                indication = ripple(bounded = true),
                                interactionSource = remember { MutableInteractionSource() }
                            ) { onFcmDetailsClick() }
                            .padding(horizontal = 4.dp)
                    )
                }

            }
        },
        actions = {
            if (showActions) {
                CircleActionIcon(
                    painter = painterResource(Resources.Icon.Call),
                    contentDescription = "Call",
                    onClick = onCall
                )
                CircleActionIcon(
                    painter = painterResource(Resources.Icon.Wapp),
                    contentDescription = "WhatsApp",
                    onClick = onWhatsApp
                )
                Spacer(Modifier.width(6.dp))
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = PrimaryBlue
        )
    )
}

@Composable
private fun CircleActionIcon(
    painter: Painter,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.size(40.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painter,
                contentDescription = contentDescription,
                tint = Color.Unspecified,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PrescriptionTopBarPreview() {
    FriendshipTheme {
        PrescriptionTopBar(
            titlePrefix = "Prescription for John Doe",
            onFcmDetailsClick = {},
            onCall = {},
            onWhatsApp = {},
            onBack = {},
            showActions = true
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PrescriptionTopBarNoActionsPreview() {
    FriendshipTheme {
        PrescriptionTopBar(
            titlePrefix = "Prescription for John Doe",
            onFcmDetailsClick = {},
            onCall = {},
            onWhatsApp = {},
            onBack = {},
            showActions = false
        )
    }
}