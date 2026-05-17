package ngo.friendship.mhealth.dc.presentation.screen.profile


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import ngo.friendship.mhealth.dc.theme.FontSize
import ngo.friendship.mhealth.dc.theme.PrimaryColor
import ngo.friendship.mhealth.dc.theme.Resources.Icon.Profile
import ngo.friendship.mhealth.dc.theme.RobotoCondensedFont
import ngo.friendship.mhealth.dc.theme.TextSecondary
import org.jetbrains.compose.resources.painterResource


data class ProfileUiState(
    val profileImage: Any? = null,
    val name: String = "",
    val designation: String = "",
    val email: String = "",
    val mobileNo: String = "",
    val versionInfo: String = "1.0.0",
    val copyrightText: String = "Copyright ©Friendship.ngo"
)

sealed class ProfileEvent {
    object OnChangePasswordClick : ProfileEvent()
    object OnHelpClick : ProfileEvent()
    object OnVideoTutorialClick : ProfileEvent()
    object OnSignOutClick : ProfileEvent()
    object OnCheckUpdateClick : ProfileEvent()
}

@Composable
fun ProfilePopup(
    uiState: ProfileUiState = ProfileUiState(),
    onDismiss: () -> Unit,
    onEvent: (ProfileEvent) -> Unit
) {
    LaunchedEffect(uiState){
            println("ProfilePopup: uiState changed: ${uiState.name}")
            println("ProfilePopup: uiState changed: ${uiState.profileImage}")
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.45f)) // Dimmed background to show current screen with low opacity
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onDismiss() }
    ) {
        Surface(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 80.dp) // Aligned right under the top bar
                .width(250.dp)
                .wrapContentHeight()
                .clickable(enabled = false) { },
            shape = RectangleShape, // No corner radius
            shadowElevation = 8.dp,
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 1. Profile Image
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color(0xFFE0E0E0), CircleShape)
                ) {
                    if (uiState.profileImage == null || (uiState.profileImage is String && (uiState.profileImage as String).isEmpty())) {
                        Image(
                            painter = painterResource(resource = Profile),
                            contentDescription = "Profile",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        SubcomposeAsyncImage(
                            model = uiState.profileImage,
                            contentDescription = "Profile",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            loading = {
                                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                   // Loading placeholder if needed
                                }
                            },
                            error = {
                                Image(
                                    painter = painterResource(resource = Profile),
                                    contentDescription = "Profile",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                // 2. Name & Designation
                Text(
                    text = uiState.name,
                    style = TextStyle(
                        fontFamily = RobotoCondensedFont(),
                        color = PrimaryColor,
                        fontSize = FontSize.REGULAR,
                        fontWeight = FontWeight.Bold
                    )
                )
                if (uiState.designation.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = uiState.designation,
                        style = TextStyle(
                            color = TextSecondary,
                            fontSize = FontSize.SMALL
                        )
                    )
                }

                if (uiState.email.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = uiState.email,
                        style = TextStyle(
                            color = TextSecondary,
                            fontSize = FontSize.SMALL
                        )
                    )
                }

                if (uiState.mobileNo.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = uiState.mobileNo,
                        style = TextStyle(
                            color = TextSecondary,
                            fontSize = FontSize.SMALL
                        )
                    )
                }

                Spacer(Modifier.height(20.dp))

                // 3. Action Buttons with Inset Dividers
                ActionList(onEvent)

                Spacer(Modifier.height(20.dp))

                // 4. Sign Out Button
                Button(
                    onClick = { onEvent(ProfileEvent.OnSignOutClick) },
                    modifier = Modifier
                        .fillMaxWidth(0.70f)
                        .height(42.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF254891)),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        "SIGN OUT",
                        style = TextStyle(fontWeight = FontWeight.Bold, color = Color.White)
                    )
                }

                Spacer(Modifier.height(24.dp))

                // 5. Footer
                ProfileFooter(uiState, onEvent)
            }
        }
    }
}

@Composable
private fun ActionList(onEvent: (ProfileEvent) -> Unit) {
    val dividerColor = Color(0xFFEEEEEE)
    val dividerWidth = 180.dp

    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        HorizontalDivider(
            modifier = Modifier.width(dividerWidth),
            thickness = 1.dp,
            color = dividerColor
        )

        ProfileMenuButton(
            text = "Change password",
            underline = true,
            onClick = { onEvent(ProfileEvent.OnChangePasswordClick) }
        )

//        HorizontalDivider(
//            modifier = Modifier.width(dividerWidth),
//            thickness = 1.dp,
//            color = dividerColor
//        )
//
//        ProfileMenuButton(
//            text = "Help",
//            onClick = { onEvent(ProfileEvent.OnHelpClick) }
//        )
//
//        HorizontalDivider(
//            modifier = Modifier.width(dividerWidth),
//            thickness = 1.dp,
//            color = dividerColor
//        )
//
//        ProfileMenuButton(
//            text = "Video Tutorial",
//            onClick = { onEvent(ProfileEvent.OnVideoTutorialClick) }
//        )
    }
}

@Composable
private fun ProfileMenuButton(
    text: String,
    underline: Boolean = false,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 6.dp)
    ) {
        Text(
            text = text,
            color = Color(0xFF333333),
            style = TextStyle(
                fontSize = FontSize.REGULAR,            // আপনার FontSize object
                fontFamily = RobotoCondensedFont(),     // আপনার font family function
                fontWeight = FontWeight.Medium,
                textDecoration = if (underline) TextDecoration.Underline else TextDecoration.None
            )
        )
    }
}

@Composable
private fun ProfileFooter(uiState: ProfileUiState, onEvent: (ProfileEvent) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Version: ${uiState.versionInfo} ",
                style = TextStyle(color = Color.Gray, fontSize = 12.sp)
            )

            Text(
                text = "Check Update",
                modifier = Modifier.clickable { onEvent(ProfileEvent.OnCheckUpdateClick) },
                style = TextStyle(
                    color = Color(0xFFFDB813), // Golden Orange
                    fontSize = 12.sp,
                    textDecoration = TextDecoration.Underline
                )
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = uiState.copyrightText,
            style = TextStyle(color = Color.Gray, fontSize = 12.sp)
        )
    }
}