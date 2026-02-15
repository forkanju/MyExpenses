package ngo.friendship.mhealth.dc.presentation.screens.main.case.dummy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ngo.friendship.mhealth.dc.theme.FontSize
import ngo.friendship.mhealth.dc.theme.Resources
import org.jetbrains.compose.resources.painterResource

@Composable
fun PrescriptionHeader(
    modifier: Modifier = Modifier,
    title: String = "Prescription",
    rightText: String = "More",
) {
    val blue = Color(0xFF214C9A)
    val grey = Color(0xFF7A7A7A)
    val iconGrey = Color(0xFF9A9A9A)

    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        Icon(
            painter = painterResource(resource = Resources.Icon.Note),
            contentDescription = "Title Icon",
            tint = blue,
            modifier = Modifier.size(32.dp)
        )

        Spacer(Modifier.width(4.dp))

        // Title
        Text(
            text = title,
            style = TextStyle(
                fontSize = FontSize.EXTRA_MEDIUM,
                fontWeight = FontWeight.Bold,
                color = blue,
            )
        )

        Spacer(Modifier.weight(1f))

        // Right "More" Section
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = rightText,
                fontSize = FontSize.REGULAR,
                fontWeight = FontWeight.Normal,
                color = grey
            )

            Spacer(Modifier.width(4.dp))

            Icon(
                painter = painterResource(resource = Resources.Icon.More),
                contentDescription = "Action Icon",
                tint = iconGrey
            )
        }
    }
}
