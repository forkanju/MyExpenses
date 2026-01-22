import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import ngo.friendship.mhealth.dc.presentation.components.CompactTextStyle
import ngo.friendship.mhealth.dc.presentation.screens.more.components.DashboardCard
import ngo.friendship.mhealth.dc.presentation.screens.more.model.DashboardCardData
import ngo.friendship.mhealth.dc.presentation.screens.more.model.SectionData
import ngo.friendship.mhealth.dc.theme.FontSize
import ngo.friendship.mhealth.dc.theme.PrimaryColor
import ngo.friendship.mhealth.dc.theme.Resources.Icon.Hand
import ngo.friendship.mhealth.dc.theme.Resources.Icon.Report
import ngo.friendship.mhealth.dc.theme.TextSecondary
import org.jetbrains.compose.resources.painterResource

@Composable
fun HealthDashboardScreen(
    modifier: Modifier = Modifier
) {
// This is the "Single Source of Truth" data list
    val sections = listOf(
        SectionData(
            title = "Urgent reports",
            cards = listOf(
                DashboardCardData(
                    "Doctor service engagement...",
                    null,
                    Report,
                    Color(0xFFF1F5FD),
                    "Download",
                    isCentered = true,
                    iconTint = Color.Unspecified
                ),
                DashboardCardData(
                    "Doctor center summary",
                    null,
                    Report,
                    Color(0xFFF1F5FD),
                    "Download",
                    isCentered = true,
                    iconTint = Color.Unspecified
                ),
                DashboardCardData(
                    "Disease wise service",
                    null,
                    Report,
                    Color(0xFFF1F5FD),
                    "Download",
                    isCentered = true,
                    iconTint = Color.Unspecified
                )
            )
        ),
        SectionData(
            title = "Resource lab",
            cards = listOf(
                DashboardCardData(
                    "Prescription templates",
                    "Templates 32+",
                    Report,
                    PrimaryColor,
                    "Create new",
                    "Import from global"
                ),
                DashboardCardData(
                    "Add DX",
                    "List 800+",
                    Report,
                    Color(0xFFF0914E),
                    "Create new"
                ),
                DashboardCardData(
                    "Medicine List",
                    "Medicine 800+",
                    Report,
                    Color(0xFF4BB652),
                    "Create new"
                ),
                DashboardCardData(
                    "Advice templates",
                    "Advice 32+",
                    Report,
                    Color(0xFF707070),
                    "Create new"
                ),
                DashboardCardData(
                    "Investigations templates",
                    "Templates 32+",
                    Report,
                    Color(0xFF707070),
                    "Create new"
                )
            )
        )
    )

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
    ) {
        val screenWidth = maxWidth

        // 1. Determine column count based on width
        val columnCount = when {
            screenWidth < 600.dp -> 3 // Phone
            screenWidth < 900.dp -> 4 // Small Tablet
            else -> 6                 // Desktop / Large Tablet
        }

        val spacing = 8.dp
        val padding = 16.dp

        // 2. Precise calculation for adaptive width
        // Formula: (Total Width - Outer Margins - Internal Gaps) / Column Count
        val itemWidth = (screenWidth - (padding * 2) - (spacing * (columnCount - 1))) / columnCount

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(padding),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            sections.forEach { section ->
                item {
                    Text(
                        text = section.title,
                        style = CompactTextStyle(
                            fontSize = FontSize.EXTRA_REGULAR,
                            fontWeight = FontWeight.SemiBold,
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // CHUNKING LOGIC: Group cards into rows based on the adaptive columnCount
                    val rows = section.cards.chunked(columnCount)

                    Column(verticalArrangement = Arrangement.spacedBy(spacing)) {
                        rows.forEach { rowCards ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(IntrinsicSize.Max), // Makes all cards in THIS specific row same height
                                horizontalArrangement = Arrangement.spacedBy(spacing)
                            ) {
                                rowCards.forEach { cardData ->
                                    DashboardCard(
                                        data = cardData,
                                        modifier = Modifier.width(itemWidth)
                                    )
                                }

                                // Fill empty space if the last row isn't full
                                repeat(columnCount - rowCards.size) {
                                    Spacer(modifier = Modifier.width(itemWidth))
                                }
                            }
                        }
                    }
                }
            }
            item {
                Text(
                    text = "Local treatment",
                    style = CompactTextStyle(
                        fontSize = FontSize.EXTRA_REGULAR,
                        color = TextSecondary,
                        fontWeight = FontWeight.SemiBold,
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                LocalTreatmentCard()
            }
        }
    }
}

@Composable
fun LocalTreatmentCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Friendship employee 33",
                    style = TextStyle(
                        
                        fontSize = FontSize.EXTRA_REGULAR,
                        color = TextSecondary,
                        fontWeight = FontWeight.Normal
                    )
                )
                Text(
                    text = "Others 16",
                    style = TextStyle(
                        
                        fontSize = FontSize.EXTRA_REGULAR,
                        color = TextSecondary,
                        fontWeight = FontWeight.Normal
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "See All",
                    color = PrimaryColor,
                    style = TextStyle(
                        fontSize = FontSize.SMALL,
                        
                        fontWeight = FontWeight.Normal
                    ),
                    textDecoration = TextDecoration.Underline
                )
            }
            Surface(
                shape = CircleShape,
                color = PrimaryColor,
                modifier = Modifier.size(48.dp),
                shadowElevation = 4.dp
            ) {
                Icon(
                    painter = painterResource(Hand),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(72.dp)
                        .padding(6.dp),
                )
            }
        }
    }
}