package ngo.friendship.mhealth.dc.presentation.screens.case.prescription_form.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ngo.friendship.mhealth.dc.presentation.screens.case.case_list.components.AvatarBadge
import ngo.friendship.mhealth.dc.theme.PrimaryColor
import ngo.friendship.mhealth.dc.theme.Resources
import ngo.friendship.mhealth.dc.theme.RobotoCondensedFont
import ngo.friendship.mhealth.dc.theme.Surface
import ngo.friendship.mhealth.dc.theme.TextDarkerGray
import org.jetbrains.compose.resources.painterResource
@Composable
fun PatientProfileCard(
    modifier: Modifier = Modifier,
    benefName: String,
    isAnsweredMode: Boolean = false
) {

    val containerColor = if (isAnsweredMode) Color(0xFFF5F5F5) else Surface
    val titleColor = if (isAnsweredMode) Color(0xFF4A4A4A) else TextDarkerGray
    val detailsColor = if (isAnsweredMode) Color(0xFF9E9E9E) else PrimaryColor
    val diseaseColor = if (isAnsweredMode) Color(0xFF616161) else PrimaryColor

    val imageFilter = if (isAnsweredMode) {
        ColorFilter.colorMatrix(
            ColorMatrix().apply { setToSaturation(0f) }
        )
    } else null

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {

        // 🧱 Main Card
        Card(
            modifier = Modifier
                .fillMaxSize(),
            shape = RoundedCornerShape(
                topStart = 0.dp,
                topEnd = 12.dp,
                bottomStart = 12.dp,
                bottomEnd = 0.dp
            ),
            colors = CardDefaults.cardColors(containerColor = containerColor)
        ) {
            Row(modifier = Modifier.padding(8.dp)) {

                AvatarBadge(
                    modifier = Modifier.size(width = 50.dp, height = 62.dp),
                    idText = "30230",
                    isAnsweredStyle = isAnsweredMode,
                    photo = {
                        Image(
                            painter = painterResource(Resources.Icon.FCM),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize(),
                            colorFilter = imageFilter
                        )
                    }
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 8.dp)
                ) {

                    Row {
                        Text(
                            text = benefName,
                            style = TextStyle(
                                color = titleColor,
                                fontFamily = RobotoCondensedFont(),
                                fontWeight = FontWeight.Medium
                            )
                        )

                        Spacer(Modifier.width(6.dp))

                        Text(
                            text = "Details",
                            style = TextStyle(
                                color = detailsColor,
                                fontFamily = RobotoCondensedFont()
                            ),
                            textDecoration = TextDecoration.Underline
                        )
                    }

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = "Oral Ulcer",
                        style = TextStyle(
                            color = diseaseColor,
                            fontFamily = RobotoCondensedFont(),
                            fontWeight = FontWeight.SemiBold
                        )
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = "Source Case  <  Case 1  <  Case 2",
                        style = TextStyle(
                            color = detailsColor,
                            fontFamily = RobotoCondensedFont()
                        )
                    )
                }
            }
        }

        // 🏷️ Answered Badge (Top Right)
        if (isAnsweredMode) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .background(
                        color = Color(0xFF7A7A7A),
                        shape = RoundedCornerShape(
                            bottomStart = 12.dp,
                            topEnd = 12.dp
                        )
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "Answered",
                    color = Color.White,
                    fontFamily = RobotoCondensedFont(),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}


@Preview(showBackground = true, name = "Simple Card Preview")
@Composable
fun SimpleCardPreview() {
    MaterialTheme {
        PatientProfileCard(benefName = "Benf Name")
    }
}