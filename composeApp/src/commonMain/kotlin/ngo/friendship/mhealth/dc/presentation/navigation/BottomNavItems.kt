package ngo.friendship.mhealth.dc.presentation.navigation

import doctorcenter.composeapp.generated.resources.Res
import doctorcenter.composeapp.generated.resources.case_n
import doctorcenter.composeapp.generated.resources.home
import doctorcenter.composeapp.generated.resources.more
import org.jetbrains.compose.resources.DrawableResource

enum class BottomNavItems(
    val iconRes: DrawableResource
) {
    Home(Res.drawable.home),
    Case(Res.drawable.case_n),
    Dashboard(Res.drawable.more)
}