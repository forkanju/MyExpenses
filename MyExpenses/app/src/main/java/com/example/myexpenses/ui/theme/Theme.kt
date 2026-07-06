package com.example.myexpenses.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color

// Local copies of colors to resolve analyzer issues
private val LocalPrimaryGreen = Color(0xFF006D3B)
private val LocalOnPrimaryGreen = Color(0xFFFFFFFF)
private val LocalPrimaryContainerGreen = Color(0xFF96F7B6)
private val LocalOnPrimaryContainerGreen = Color(0xFF00210E)
private val LocalSecondaryGreen = Color(0xFF4F6353)
private val LocalOnSecondaryGreen = Color(0xFFFFFFFF)
private val LocalSecondaryContainerGreen = Color(0xFFD2E8D4)
private val LocalOnSecondaryContainerGreen = Color(0xFF0D1F13)
private val LocalTertiaryGreen = Color(0xFF3B6470)
private val LocalOnTertiaryGreen = Color(0xFFFFFFFF)
private val LocalTertiaryContainerGreen = Color(0xFFBEEAF7)
private val LocalOnTertiaryContainerGreen = Color(0xFF001F26)
private val LocalErrorRed = Color(0xFFBA1A1A)
private val LocalOnErrorRed = Color(0xFFFFFFFF)
private val LocalErrorContainerRed = Color(0xFFFFDAD6)
private val LocalOnErrorContainerRed = Color(0xFF410002)
private val LocalBackgroundLight = Color(0xFFFBFDF8)
private val LocalOnBackgroundLight = Color(0xFF191C19)
private val LocalSurfaceLight = Color(0xFFFBFDF8)
private val LocalOnSurfaceLight = Color(0xFF191C19)

private val LocalPrimaryGreenDark = Color(0xFF7BDA9C)
private val LocalOnPrimaryGreenDark = Color(0xFF00391C)
private val LocalPrimaryContainerGreenDark = Color(0xFF00522B)
private val LocalOnPrimaryContainerGreenDark = Color(0xFF96F7B6)
private val LocalSecondaryGreenDark = Color(0xFFB6CCB9)
private val LocalOnSecondaryGreenDark = Color(0xFF223527)
private val LocalSecondaryContainerGreenDark = Color(0xFF384B3C)
private val LocalOnSecondaryContainerGreenDark = Color(0xFFD2E8D4)
private val LocalTertiaryGreenDark = Color(0xFFA2CEDB)
private val LocalOnTertiaryGreenDark = Color(0xFF013641)
private val LocalTertiaryContainerGreenDark = Color(0xFF214C58)
private val LocalOnTertiaryContainerGreenDark = Color(0xFFBEEAF7)
private val LocalBackgroundDark = Color(0xFF191C19)
private val LocalOnBackgroundDark = Color(0xFFE1E3DE)
private val LocalSurfaceDark = Color(0xFF191C19)
private val LocalOnSurfaceDark = Color(0xFFE1E3DE)

private val MyDarkColorScheme = darkColorScheme(
    primary = LocalPrimaryGreenDark,
    onPrimary = LocalOnPrimaryGreenDark,
    primaryContainer = LocalPrimaryContainerGreenDark,
    onPrimaryContainer = LocalOnPrimaryContainerGreenDark,
    secondary = LocalSecondaryGreenDark,
    onSecondary = LocalOnSecondaryGreenDark,
    secondaryContainer = LocalSecondaryContainerGreenDark,
    onSecondaryContainer = LocalOnSecondaryContainerGreenDark,
    tertiary = LocalTertiaryGreenDark,
    onTertiary = LocalOnTertiaryGreenDark,
    tertiaryContainer = LocalTertiaryContainerGreenDark,
    onTertiaryContainer = LocalOnTertiaryContainerGreenDark,
    error = LocalErrorRed,
    onError = LocalOnErrorRed,
    errorContainer = LocalErrorContainerRed,
    onErrorContainer = LocalOnErrorContainerRed,
    background = LocalBackgroundDark,
    onBackground = LocalOnBackgroundDark,
    surface = LocalSurfaceDark,
    onSurface = LocalOnSurfaceDark
)

private val MyLightColorScheme = lightColorScheme(
    primary = LocalPrimaryGreen,
    onPrimary = LocalOnPrimaryGreen,
    primaryContainer = LocalPrimaryContainerGreen,
    onPrimaryContainer = LocalOnPrimaryContainerGreen,
    secondary = LocalSecondaryGreen,
    onSecondary = LocalOnSecondaryGreen,
    secondaryContainer = LocalSecondaryContainerGreen,
    onSecondaryContainer = LocalOnSecondaryContainerGreen,
    tertiary = LocalTertiaryGreen,
    onTertiary = LocalOnTertiaryGreen,
    tertiaryContainer = LocalTertiaryContainerGreen,
    onTertiaryContainer = LocalOnTertiaryContainerGreen,
    error = LocalErrorRed,
    onError = LocalOnErrorRed,
    errorContainer = LocalErrorContainerRed,
    onErrorContainer = LocalOnErrorContainerRed,
    background = LocalBackgroundLight,
    onBackground = LocalOnBackgroundLight,
    surface = LocalSurfaceLight,
    onSurface = LocalOnSurfaceLight
)

@Composable
fun MyExpensesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> MyDarkColorScheme
        else -> MyLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
