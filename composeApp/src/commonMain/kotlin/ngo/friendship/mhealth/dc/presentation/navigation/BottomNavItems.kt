package ngo.friendship.mhealth.dc.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cases
import androidx.compose.material.icons.rounded.Dashboard
import androidx.compose.material.icons.rounded.Home
import androidx.compose.ui.graphics.vector.ImageVector

enum class BottomNavItems(val icon: ImageVector) {
    Home(Icons.Rounded.Home),
    Case(Icons.Rounded.Cases),
    Dashboard(Icons.Rounded.Dashboard)
}