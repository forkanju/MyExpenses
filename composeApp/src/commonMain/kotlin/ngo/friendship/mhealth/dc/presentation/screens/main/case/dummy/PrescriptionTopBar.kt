package ngo.friendship.mhealth.dc.presentation.screens.main.case.dummy

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ngo.friendship.mhealth.dc.theme.RobotoCondensedFont

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrescriptionTopBar(
    titlePrefix: String,              // e.g. "Ref by Most Rina- Cox's Bazar ..."
    onBack: () -> Unit,
    onViewClick: () -> Unit,
    onCall: () -> Unit,
    onWhatsApp: () -> Unit,
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF444444)
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
                        letterSpacing = 0.sp
                    )
                )


                Spacer(Modifier.width(6.dp))

                Text(
                    text = "(View)",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = RobotoCondensedFont(),
                    letterSpacing = 0.sp,
                    color = Color(0xFF1E88E5),
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { onViewClick() }
                )

            }
        },
        actions = {
            CircleActionIcon(
                icon = Icons.Default.Phone,
                contentDescription = "Call",
                iconTint = Color.White,
                bgColor = Color(0xFF1E88E5),
                onClick = onCall
            )
            Spacer(Modifier.width(10.dp))
            CircleActionIcon(
                icon = Icons.Default.Chat, // WhatsApp icon নেই default set-এ
                contentDescription = "WhatsApp",
                iconTint = Color.White,
                bgColor = Color(0xFF25D366),
                onClick = onWhatsApp
            )
            Spacer(Modifier.width(6.dp))
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White
        )
    )
}

@Composable
private fun CircleActionIcon(
    icon: ImageVector,
    contentDescription: String,
    bgColor: Color,
    iconTint: Color,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.size(40.dp)
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(bgColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = iconTint,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}