package ngo.friendship.mhealth.dc.presentation.navigation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import ngo.friendship.mhealth.dc.presentation.navigation.BottomNavItems
import ngo.friendship.mhealth.dc.theme.BottomBarUnselected
import ngo.friendship.mhealth.dc.theme.CardBackground
import ngo.friendship.mhealth.dc.theme.Dimen
import ngo.friendship.mhealth.dc.theme.FontSize.REGULAR
import ngo.friendship.mhealth.dc.theme.PrimaryColor
import org.jetbrains.compose.resources.painterResource

@Composable
fun BottomBar(
    pagerState: PagerState = rememberPagerState(initialPage = 0) { BottomNavItems.entries.size },
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val width = LocalWindowInfo.current.containerDpSize.width
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = CardBackground,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = Dimen.Large),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            BottomNavItems.entries.forEachIndexed { index, screen ->
                val animatedTint by animateColorAsState(
                    targetValue = if (pagerState.currentPage == index) PrimaryColor else BottomBarUnselected
                )
                val interactionSource = remember { MutableInteractionSource() }

                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(Dimen.Standard))
                        .clickable(
                            interactionSource = interactionSource,
                            indication = ripple(bounded = true)
                        ) { onItemClick(index) }
                        .width(
                            width
                                .minus(Dimen.Large * 2)
                                .div(BottomNavItems.entries.size)
                                .minus(Dimen.StandardPlus)
                        )
                        .padding(vertical = Dimen.Medium),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(screen.iconRes),
                        contentDescription = screen.name,
                        tint = animatedTint
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = screen.name,
                        color = animatedTint,
                        fontSize = REGULAR,
                        fontWeight = if (pagerState.currentPage == index) FontWeight.Bold else FontWeight.Normal,
                        textDecoration = if (pagerState.currentPage == index) TextDecoration.Underline else TextDecoration.None
                    )
                }
            }
        }
    }
}
