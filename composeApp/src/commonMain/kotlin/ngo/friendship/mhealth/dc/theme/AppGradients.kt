package ngo.friendship.mhealth.dc.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

object AppGradients {
    // Primary gradient with 0%, 50%, 100% stops
    val Primary = GradientDefinition(
        lightColors = listOf(
            Color(0xFFE8F5E8), // 0%
            Color(0xFFF8FDF8), // 50%
            Color(0xFFDCF5DD)  // 100%
        ),
        darkColors = listOf(
            Color(0xFF141E30), // 0%
            Color(0xFF243B55), // 50%
            Color(0xFF141E30)  // 100%
        ),
        stops = listOf(0.0f, 0.5f, 1.0f)
    )

    // Secondary gradient
    val Secondary = GradientDefinition(
        lightColors = listOf(
            Color(0xFFE3F2FD),
            Color(0xFFF3E5F5),
            Color(0xFFE8EAF6)
        ),
        darkColors = listOf(
            Color(0xFF1A237E),
            Color(0xFF283593),
            Color(0xFF303F9F)
        ),
        stops = listOf(0.0f, 0.5f, 1.0f)
    )

    // Surface gradient
    val Surface = GradientDefinition(
        lightColors = listOf(
            Color(0xFFFFFFFF),
            Color(0xFFF5F5F5),
            Color(0xFFEEEEEE)
        ),
        darkColors = listOf(
            Color(0xFF121212),
            Color(0xFF1E1E1E),
            Color(0xFF2D2D2D)
        ),
        stops = listOf(0.0f, 0.5f, 1.0f)
    )

    // Error gradient
    val Error = GradientDefinition(
        lightColors = listOf(
            Color(0xFFFFEBEE),
            Color(0xFFFFCDD2),
            Color(0xFFEF9A9A)
        ),
        darkColors = listOf(
            Color(0xFFB71C1C),
            Color(0xFFC62828),
            Color(0xFFD32F2F)
        ),
        stops = listOf(0.0f, 0.5f, 1.0f)
    )

    // Success gradient
    val Success = GradientDefinition(
        lightColors = listOf(
            Color(0xFFE8F5E8),
            Color(0xFFC8E6C9),
            Color(0xFFA5D6A7)
        ),
        darkColors = listOf(
            Color(0xFF1B5E20),
            Color(0xFF2E7D32),
            Color(0xFF388E3C)
        ),
        stops = listOf(0.0f, 0.5f, 1.0f)
    )

    // Default gradient (primary)
    val Default = Primary
}

// Data class to hold gradient definition
data class GradientDefinition(
    val lightColors: List<Color>,
    val darkColors: List<Color>,
    val stops: List<Float> = emptyList()
) {
    @Composable
    fun toBrush(): Brush {
        val colors = if (isSystemInDarkTheme()) darkColors else lightColors

        return if (stops.isNotEmpty() && stops.size == colors.size) {
            // FIX: Use stops.zip(colors) instead of colors.zip(stops)
            Brush.linearGradient(
                colorStops = stops.zip(colors).toTypedArray(), // Correct order: Float first, then Color
                start = Offset(0f, 0f),
                end = Offset(0f, Float.POSITIVE_INFINITY)
            )
        } else {
            Brush.linearGradient(
                colors = colors,
                start = Offset(0f, 0f),
                end = Offset(0f, Float.POSITIVE_INFINITY)
            )
        }
    }
}