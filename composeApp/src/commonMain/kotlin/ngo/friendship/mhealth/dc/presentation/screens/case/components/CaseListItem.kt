package ngo.friendship.mhealth.dc.presentation.screens.case.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ngo.friendship.mhealth.dc.presentation.components.CompactTextStyle
import ngo.friendship.mhealth.dc.presentation.screens.case.model.CaseItemUi
import ngo.friendship.mhealth.dc.presentation.theme.FontSize
import ngo.friendship.mhealth.dc.presentation.theme.PrimaryColor
import org.jetbrains.compose.resources.painterResource


@Composable
fun CaseListItem(ui: CaseItemUi, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(
            topStart = 0.dp,
            topEnd = 0.dp,
            bottomEnd = 12.dp,
            bottomStart = 12.dp
        ),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                AvatarTileWithBadges(
                    modifier = Modifier.size(width = 56.dp, height = 68.dp),
                    timeText = "00:25",
                    idText = "30230",
                    photo = {
                        Image(
                            painter = painterResource(ngo.friendship.mhealth.dc.presentation.theme.Resources.Icon.FCM),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                )

                Spacer(Modifier.width(10.dp))

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        "${ui.name} (${ui.gender} | ${ui.age}y)",
                        style = CompactTextStyle(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        ui.locationLine,
                        style = CompactTextStyle(
                            fontSize = FontSize.SMALL,
                            fontStyle = FontStyle.Italic
                        )
                    )
                    Text(
                        ui.problemTitle,
                        style = CompactTextStyle(
                            fontSize = FontSize.MEDIUM,
                            color = PrimaryColor,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }

            Spacer(Modifier.height(8.dp))
            DottedDivider()
            Spacer(Modifier.height(4.dp))

            // Footer Section
            Text(
                "Ref by: ${ui.refBy}",
                style = CompactTextStyle(
                    fontSize = FontSize.EXTRA_SMALL,
                    fontStyle = FontStyle.Italic,
                    color = Color.Gray
                )
            )
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "at",
                    style = CompactTextStyle(
                        fontSize = FontSize.EXTRA_SMALL,
                        fontStyle = FontStyle.Italic,
                        color = Color.Gray
                    )
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    ui.atText,
                    style = CompactTextStyle(
                        fontSize = FontSize.EXTRA_SMALL,
                        fontStyle = FontStyle.Italic,
                        color = Color(0xFFE25555),
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    ui.interviewText,
                    style = CompactTextStyle(fontSize = FontSize.EXTRA_SMALL, color = Color.Gray),
                    fontStyle = FontStyle.Italic,
                )
            }
        }
    }
}

