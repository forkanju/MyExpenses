package ngo.friendship.mhealth.dc.presentation.screens.case.case_detail.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ngo.friendship.mhealth.dc.theme.FontSize
import ngo.friendship.mhealth.dc.theme.IconSecondary
import ngo.friendship.mhealth.dc.theme.PrimaryColor
import ngo.friendship.mhealth.dc.theme.Resources
import ngo.friendship.mhealth.dc.theme.RobotoCondensedFont
import ngo.friendship.mhealth.dc.theme.TextSecondary
import org.jetbrains.compose.resources.painterResource

@Composable
fun PrescriptionHeader(
    modifier: Modifier = Modifier,
    title: String = "Prescription",
    rightText: String = "More",
    isAnsweredMode: Boolean = false,
    onMoreClick: () -> Unit = {}
) {
    val titleColor = if (isAnsweredMode) Color(0xFF666666) else PrimaryColor
    val rightTextColor = if (isAnsweredMode) Color(0xFF7A7A7A) else TextSecondary
    val leftIconColor = if (isAnsweredMode) Color(0xFF666666) else PrimaryColor
    val rightIconColor = if (isAnsweredMode) Color(0xFF8A8A8A) else IconSecondary

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(resource = Resources.Icon.Note),
            contentDescription = "Title Icon",
            tint = leftIconColor,
            modifier = Modifier.size(32.dp)
        )

        Spacer(Modifier.width(4.dp))

        Text(
            text = title,
            style = TextStyle(
                fontSize = FontSize.EXTRA_MEDIUM,
                fontFamily = RobotoCondensedFont(),
                fontWeight = FontWeight.Bold,
                color = titleColor,
            )
        )

        Spacer(Modifier.weight(1f))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.clickable { onMoreClick() }
        ) {
            Text(
                text = rightText,
                fontSize = FontSize.REGULAR,
                fontWeight = FontWeight.Normal,
                fontFamily = RobotoCondensedFont(),
                color = rightTextColor
            )

            Spacer(Modifier.width(4.dp))

            Icon(
                painter = painterResource(resource = Resources.Icon.More),
                contentDescription = "More Icon",
                tint = rightIconColor
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PrescriptionHeaderPreview() {
    MaterialTheme {
        PrescriptionHeader(isAnsweredMode = true)
    }
}