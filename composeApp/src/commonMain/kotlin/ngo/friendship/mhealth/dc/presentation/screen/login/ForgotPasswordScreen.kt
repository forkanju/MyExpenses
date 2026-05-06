package ngo.friendship.mhealth.dc.presentation.screen.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ngo.friendship.mhealth.dc.presentation.components.PrimaryButton
import ngo.friendship.mhealth.dc.theme.ButtonPrimary
import ngo.friendship.mhealth.dc.theme.CanvasBackground
import ngo.friendship.mhealth.dc.theme.FontSize
import ngo.friendship.mhealth.dc.theme.FriendshipTheme
import ngo.friendship.mhealth.dc.theme.TextPrimary

@Composable
fun ForgotPasswordScreen(
    onBackToLoginClick: () -> Unit = {}
) {
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp, vertical = 16.dp)
            .imePadding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Title
        Text(
            text = "Forgot Password",
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = FontSize.LARGE,
                color = TextPrimary
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Click below button to reset your password from mHealth web portal.",
            style = MaterialTheme.typography.bodyMedium.copy(

                fontSize = FontSize.REGULAR,
                fontWeight = FontWeight.Normal,
                color = TextPrimary
            )
        )

        Spacer(modifier = Modifier.height(28.dp))

        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
            text = "Click here to reset your password",
            onClick = {
                uriHandler.openUri("https://mhealth.apps.friendship.ngo/mHealth/")
            },
            enabled = true,
            modifier = Modifier.fillMaxWidth(),
            isUnderlined = true,
            buttonBackgroundColor = CanvasBackground,
            buttonTextColor = ButtonPrimary,
            border = BorderStroke(1.dp, ButtonPrimary)
        )

        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
            text = "Back to login",
            onClick = onBackToLoginClick,
            enabled = true,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FPPrev() {
    FriendshipTheme {
        ForgotPasswordScreen()
    }
}
