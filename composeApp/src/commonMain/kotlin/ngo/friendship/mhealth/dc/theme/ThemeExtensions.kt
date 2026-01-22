package ngo.friendship.mhealth.dc.theme

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

object ThemeExtensions {

    // Default theme gradient (primary)
    @Composable
    fun Modifier.themeGradient(): Modifier {
        return this.background(brush = AppGradients.Default.toBrush())
    }

    // Primary gradient
    @Composable
    fun Modifier.primaryGradient(): Modifier {
        return this.background(brush = AppGradients.Primary.toBrush())
    }

    // Secondary gradient
    @Composable
    fun Modifier.secondaryGradient(): Modifier {
        return this.background(brush = AppGradients.Secondary.toBrush())
    }

    // Surface gradient
    @Composable
    fun Modifier.surfaceGradient(): Modifier {
        return this.background(brush = AppGradients.Surface.toBrush())
    }

    // Error gradient
    @Composable
    fun Modifier.errorGradient(): Modifier {
        return this.background(brush = AppGradients.Error.toBrush())
    }

    // Success gradient
    @Composable
    fun Modifier.successGradient(): Modifier {
        return this.background(brush = AppGradients.Success.toBrush())
    }

    // Custom gradient from definition
    @Composable
    fun Modifier.customGradient(gradient: GradientDefinition): Modifier {
        return this.background(brush = gradient.toBrush())
    }

    // Direct brush accessors (for cases where you need the brush directly)
    @Composable
    fun themeBrush() = AppGradients.Default.toBrush()

    @Composable
    fun primaryBrush() = AppGradients.Primary.toBrush()

    @Composable
    fun secondaryBrush() = AppGradients.Secondary.toBrush()

    @Composable
    fun surfaceBrush() = AppGradients.Surface.toBrush()

    @Composable
    fun errorBrush() = AppGradients.Error.toBrush()

    @Composable
    fun successBrush() = AppGradients.Success.toBrush()
}