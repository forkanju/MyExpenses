package ngo.friendship.mhealth.dc.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val lightScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = White,
    primaryContainer = PrimaryGreen,
    onPrimaryContainer = White,
    secondary = SecondaryWhite,
    onSecondary = Black,
    error = ErrorRed,
    onError = White,
    background = Gray100,
    onBackground = Gray900,
    surface = White,
    onSurface = Gray900,
    surfaceVariant = AnsweredSurface,
    onSurfaceVariant = AnsweredTextPrimary,
    outline = Gray300,
    outlineVariant = AnsweredBorder,
    inverseSurface = AnsweredBackground,
    inverseOnSurface = AnsweredAction
)

private val darkScheme = darkColorScheme(
    primary = PrimaryGreen,
    onPrimary = Gray900,
    primaryContainer = PrimaryBlue,
    onPrimaryContainer = White,
    secondary = Gray700,
    onSecondary = White,
    error = Red,
    onError = White,
    background = Black,
    onBackground = White,
    surface = Gray900,
    onSurface = White,
    outline = Gray500
)

@Composable
fun FriendshipTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colorScheme = if (darkTheme) darkScheme else lightScheme

    MaterialTheme(
        colorScheme = colorScheme,
        motionScheme = MotionScheme.expressive(),
        shapes = AppShapes,
        typography = appTypography(),
        content = content,
    )
}

