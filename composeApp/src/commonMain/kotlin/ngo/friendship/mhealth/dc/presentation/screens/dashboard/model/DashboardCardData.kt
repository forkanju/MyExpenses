package ngo.friendship.mhealth.dc.presentation.screens.main.dashboard.model

import androidx.compose.ui.graphics.Color
import ngo.friendship.mhealth.dc.theme.Resources.Icon.Report
import org.jetbrains.compose.resources.DrawableResource

data class DashboardCardData(
    val title: String,
    val subtitle: String? = null,
    val icon: DrawableResource = Report,
    val iconBackground: Color,
    val actionText: String,
    val secondaryActionText: String? = null,
    val isCentered: Boolean = false,
    val iconTint: Color = Color.White,
)

data class SectionData(
    val title: String,
    val cards: List<DashboardCardData>
)
