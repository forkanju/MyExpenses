package ngo.friendship.mhealth.dc.presentation.screens.main.dashboard.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ngo.friendship.mhealth.dc.presentation.components.CompactTextStyle
import ngo.friendship.mhealth.dc.presentation.screens.main.dashboard.model.DashboardCardData
import ngo.friendship.mhealth.dc.theme.FontSize
import ngo.friendship.mhealth.dc.theme.PrimaryColor
import ngo.friendship.mhealth.dc.theme.TextSecondary
import org.jetbrains.compose.resources.painterResource


@Composable
fun DashboardCard(data: DashboardCardData, modifier: Modifier = Modifier) {
    val alignment = if (data.isCentered) Alignment.CenterHorizontally else Alignment.Start
    val textAlign = if (data.isCentered) TextAlign.Center else TextAlign.Start
    val iconContainerSize = if (data.isCentered) 48.dp else 36.dp
    val iconSize = if (data.isCentered) 40.dp else 34 .dp

    Card(
        // fillMaxHeight is crucial for the card to stretch to the parent's Intrinsic height
        modifier = modifier.fillMaxHeight(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(0.5.dp, Color.LightGray.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = alignment
        ) {
            // Header: Icon and Optional Secondary Action
            Box(modifier = Modifier.fillMaxWidth()) {
                Surface(
                    shape = CircleShape,
                    color = data.iconBackground,
                    modifier = Modifier
                        .size(iconContainerSize)
                        .align(if (data.isCentered) Alignment.Center else Alignment.TopStart)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(data.icon),
                            contentDescription = null,
                            tint = data.iconTint,
                            modifier = Modifier
                                .size(iconSize)
                                .padding(8.dp),
                        )
                    }
                }

                if (data.secondaryActionText != null) {
                    Text(
                        text = data.secondaryActionText,
                        style = CompactTextStyle(
                            fontSize = FontSize.EXTRA_SMALL,
                            textAlign = TextAlign.End,
                            textDecoration = TextDecoration.Underline,
                            color = PrimaryColor
                        ),
                        lineHeight = 10.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .width(50.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Title
            Text(
                text = data.title,
                style = CompactTextStyle(
                    fontSize = FontSize.REGULAR,
                    textAlign = textAlign
                ).copy(lineHeight = 16.sp),
                maxLines = 2,
                modifier = Modifier.fillMaxWidth()
            )

            // Subtitle (Optional - this is what often makes heights uneven)
            if (data.subtitle != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = data.subtitle,
                    style = CompactTextStyle(
                        fontSize = FontSize.EXTRA_SMALL,
                        textAlign = textAlign
                    )
                )
            }

            // --- THE KEY ADDITION ---
            // This spacer pushes the Action Text to the bottom.
            // In shorter cards, this weight will expand to match the tallest card in the Row.
            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(8.dp))

            // Footer Action
            Text(
                text = data.actionText,
                style = CompactTextStyle(
                    fontSize = FontSize.SMALL,
                    textDecoration = TextDecoration.Underline,
                    textAlign = textAlign,
                    color = if (data.actionText == "Download") TextSecondary else PrimaryColor
                )
            )
        }
    }
}