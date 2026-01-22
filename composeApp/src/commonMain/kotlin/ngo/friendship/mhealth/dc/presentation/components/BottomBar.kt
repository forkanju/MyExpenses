package ngo.friendship.mhealth.dc.presentation.components

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import ngo.friendship.mhealth.dc.presentation.state.BottomBarDestination
import ngo.friendship.mhealth.dc.theme.BottomBarUnselected
import ngo.friendship.mhealth.dc.theme.CardBackground
import ngo.friendship.mhealth.dc.theme.FontSize.REGULAR
import ngo.friendship.mhealth.dc.theme.PrimaryColor
import org.jetbrains.compose.resources.painterResource

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    selected: BottomBarDestination,
    onSelect: (BottomBarDestination) -> Unit
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = CardBackground,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 36.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            BottomBarDestination.entries.forEach { destination ->
                val animatedTint by animateColorAsState(
                    targetValue = if (selected == destination) PrimaryColor else BottomBarUnselected
                )
                val interactionSource = remember { MutableInteractionSource() }

                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable(
                            interactionSource = interactionSource,
                            indication = ripple(bounded = true)
                        ) { onSelect(destination) },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(destination.icon),
                        contentDescription = destination.title,
                        tint = animatedTint
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = destination.title,
                        color = animatedTint,
                        fontSize = REGULAR,
                        fontWeight = if (selected == destination) FontWeight.Bold else FontWeight.Normal,
                        textDecoration = if (selected == destination) TextDecoration.Underline else TextDecoration.None
                    )
                }
            }
        }
    }
}
