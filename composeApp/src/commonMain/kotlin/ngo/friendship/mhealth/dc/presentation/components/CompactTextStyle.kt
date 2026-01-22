package ngo.friendship.mhealth.dc.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import ngo.friendship.mhealth.dc.presentation.theme.FontSize
import ngo.friendship.mhealth.dc.presentation.theme.RobotoCondensedFont
import ngo.friendship.mhealth.dc.presentation.theme.TextSecondary

@Composable
fun CompactTextStyle(
    fontSize: TextUnit = FontSize.REGULAR,
    color: Color = TextSecondary,
    fontWeight: FontWeight = FontWeight.Normal,
    fontStyle: FontStyle = FontStyle.Normal,
    textDecoration: TextDecoration = TextDecoration.None,
    textAlign: TextAlign = TextAlign.Start
) = TextStyle(
    fontSize = fontSize,
    color = color,
    fontWeight = fontWeight,
    fontStyle = fontStyle,
    fontFamily = RobotoCondensedFont(),
    textDecoration = textDecoration,
    textAlign = textAlign,
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.Both
    )
)
