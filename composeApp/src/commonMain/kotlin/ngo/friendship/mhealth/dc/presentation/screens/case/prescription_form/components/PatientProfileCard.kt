package ngo.friendship.mhealth.dc.presentation.screens.main.prescription_form.components

import androidx.compose.foundation.Image
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
import androidx.compose.ui.Modifier
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
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(90.dp),
        shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Surface
        )
    ) {

        Row(
            modifier = Modifier.padding(8.dp)
        ) {
            AvatarBadge(
                modifier = Modifier.size(width = 50.dp, height = 62.dp),
                idText = "567876",
                photo = {
                    Image(
                        painter = painterResource(Resources.Icon.FCM),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            )
            Column(
                modifier = Modifier.fillMaxSize().padding(8.dp),
            ) {
                Row(

                ) {
                    Text(
                        text = "Most Kabita Bala Ismat (43y | F)",
                        style = TextStyle(
                            color = TextDarkerGray,
                            fontFamily = RobotoCondensedFont(),
                            fontWeight = FontWeight.Normal
                        )
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "Details",
                        style = TextStyle(
                            color = PrimaryColor,
                            fontFamily = RobotoCondensedFont(),
                            fontWeight = FontWeight.Normal
                        ),
                        textDecoration = TextDecoration.Underline
                    )
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Oral Ulcer",
                    style = TextStyle(
                        color = PrimaryColor,
                        fontFamily = RobotoCondensedFont(),
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }
    }
}


@Preview(
    showBackground = true,
    name = "Simple Card Preview"
)
@Composable
fun SimpleCardPreview() {
    MaterialTheme {
        PatientProfileCard()
    }
}
