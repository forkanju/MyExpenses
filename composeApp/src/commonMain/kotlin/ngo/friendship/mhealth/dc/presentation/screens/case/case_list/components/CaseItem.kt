package ngo.friendship.mhealth.dc.presentation.screens.case.case_list.components

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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ngo.friendship.mhealth.dc.domain.model.Interview
import ngo.friendship.mhealth.dc.presentation.components.CompactTextStyle
import ngo.friendship.mhealth.dc.presentation.components.DottedDivider
import ngo.friendship.mhealth.dc.theme.FontSize
import ngo.friendship.mhealth.dc.theme.PrimaryColor
import ngo.friendship.mhealth.dc.theme.Resources
import ngo.friendship.mhealth.dc.utils.toUiDateTime
import org.jetbrains.compose.resources.painterResource

/**
 * A composable that displays a single case item in a list.
 *
 * @param ui The [Interview] data to be displayed.
 * @param onClick Callback to be invoked when the item is clicked.
 * @param isAnsweredStyle Whether to apply a grayscale/answered visual style to the item.
 */
@Composable
fun CaseItem(
    ui: Interview,
    onClick: () -> Unit,
    isAnsweredStyle: Boolean = false
) {
    val cardContainerColor = if (isAnsweredStyle) Color.White else Color.White
    val titleColor = Color.Black
    val locationColor = Color.Gray
    val questionColor = if (isAnsweredStyle) Color.Black else PrimaryColor
    val refColor = Color.Gray
    val timeColor = if (isAnsweredStyle) Color.Black else Color(0xFFE25555)

    val grayscaleFilter = if (isAnsweredStyle) {
        ColorFilter.colorMatrix(
            ColorMatrix().apply { setToSaturation(0f) }
        )
    } else null

    Card(
        onClick = onClick,
        shape = RoundedCornerShape(
            topStart = 0.dp,
            topEnd = 0.dp,
            bottomEnd = 12.dp,
            bottomStart = 12.dp
        ),
        colors = CardDefaults.cardColors(containerColor = cardContainerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                AvatarTileWithBadges(
                    modifier = Modifier.size(width = 56.dp, height = 68.dp),
                    timeText = ui.startTime,
                    idText = ui.beneficiaryCode.substringAfter("-"),
                    isAnsweredStyle = isAnsweredStyle,
                    photo = {
                        Image(
                            painter = painterResource(Resources.Icon.FCM),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize(),
                            colorFilter = grayscaleFilter
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
                        text = "${ui.beneficiaryName} (${ui.status})",
                        style = CompactTextStyle(
                            fontWeight = FontWeight.Bold,
                            color = titleColor
                        )
                    )

                    Text(
                        text = ui.location,
                        style = CompactTextStyle(
                            fontSize = FontSize.SMALL,
                            fontStyle = FontStyle.Italic,
                            color = locationColor
                        )
                    )

                    Text(
                        text = ui.questionnaireName,
                        style = CompactTextStyle(
                            fontSize = FontSize.MEDIUM,
                            color = questionColor,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }

            Spacer(Modifier.height(8.dp))
            DottedDivider()
            Spacer(Modifier.height(4.dp))

            Text(
                text = "Ref by: ${ui.userName}",
                style = CompactTextStyle(
                    fontSize = FontSize.EXTRA_SMALL,
                    fontStyle = FontStyle.Italic,
                    color = refColor
                )
            )

            Spacer(Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "at",
                    style = CompactTextStyle(
                        fontSize = FontSize.EXTRA_SMALL,
                        fontStyle = FontStyle.Italic,
                        color = refColor
                    )
                )

                Spacer(Modifier.width(4.dp))

                Text(
                    text = ui.startTime.toUiDateTime(),
                    style = CompactTextStyle(
                        fontSize = FontSize.EXTRA_SMALL,
                        fontStyle = FontStyle.Italic,
                        color = timeColor,
                        fontWeight = FontWeight.SemiBold
                    )
                )

                Spacer(Modifier.width(4.dp))

                Text(
                    text = ui.status,
                    style = CompactTextStyle(
                        fontSize = FontSize.EXTRA_SMALL,
                        color = refColor
                    ),
                    fontStyle = FontStyle.Italic,
                )
            }
        }
    }
}