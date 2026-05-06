package ngo.friendship.mhealth.dc.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import ngo.friendship.mhealth.dc.theme.ButtonDisabled
import ngo.friendship.mhealth.dc.theme.ButtonPrimary
import ngo.friendship.mhealth.dc.theme.ButtonTextDisabled
import ngo.friendship.mhealth.dc.theme.ButtonTextPrimary
import ngo.friendship.mhealth.dc.theme.FontSize
import ngo.friendship.mhealth.dc.theme.TextWhite

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isUnderlined: Boolean = false,
    buttonBackgroundColor: Color = ButtonPrimary,
    buttonTextColor: Color = TextWhite,
    border: BorderStroke? = null
) {

    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(4.dp),
        border = border,
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonBackgroundColor,
            disabledContainerColor = ButtonDisabled,
            contentColor = ButtonTextPrimary,
            disabledContentColor = ButtonTextDisabled
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge.copy(
                
                fontSize = FontSize.EXTRA_REGULAR,
                fontWeight = FontWeight.Bold,
                color = buttonTextColor,
                textDecoration = if (isUnderlined) TextDecoration.Underline else null
            )
        )


    }
}