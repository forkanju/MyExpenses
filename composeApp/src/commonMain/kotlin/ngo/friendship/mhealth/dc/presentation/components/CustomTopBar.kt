package ngo.friendship.mhealth.dc.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ngo.friendship.mhealth.dc.theme.Dimen
import ngo.friendship.mhealth.dc.theme.FontSize
import ngo.friendship.mhealth.dc.theme.Red
import ngo.friendship.mhealth.dc.theme.Resources.Icon.AppLogoWhite
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun CustomTopBar(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFF1F4E96),
    onLogoClick: () -> Unit = {},
    notificationIcon: DrawableResource,
    notificationCount: Int = 100,
    onNotificationClick: () -> Unit,
    userName: String,
    userSubtitle: String? = null,
    profileIcon: DrawableResource,
    onProfileClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .statusBarsPadding()
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppLogo(iconLogo = AppLogoWhite)
        Spacer(Modifier.weight(1f))
        BadgedBox(
            badge = {
                DynamicCountBadge(
                    notificationCount,
                    borderColor = Color.White,
                    modifier = Modifier.offset(x = (-4).dp, y = 4.dp)
                )
            }
        ) {
            IconButton(onClick = onNotificationClick) {
                Icon(
                    painter = painterResource(notificationIcon),
                    contentDescription = "Notifications",
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        Spacer(Modifier.width(Dimen.Standard))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
        ) {
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.widthIn(max = 120.dp),) {
                Text(
                    text = userName,
                    color = Color.White,
                    fontWeight = FontWeight.Medium,
                    fontSize = FontSize.SMALL,
                    maxLines = 1,
                    lineHeight = 14.sp,
                    overflow = TextOverflow.Ellipsis
                )

                if (!userSubtitle.isNullOrBlank()) {
                    Text(
                        text = userSubtitle,
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = FontSize.EXTRA_SMALL,
                        maxLines = 1,
                        lineHeight = 12.sp,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Spacer(Modifier.width(10.dp))
            IconButton(onClick = onProfileClick) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.18f))
                        .border(
                            width = 1.dp,
                            color = Color.White.copy(alpha = 0.6f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(profileIcon),
                        contentDescription = "Profile",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun DynamicCountBadge(
    count: Int,
    modifier: Modifier = Modifier,
    minSize: Dp = 18.dp,
    backgroundColor: Color = Red,
    textColor: Color = Color.White,
    borderColor: Color? = null
) {
    if (count <= 0) return
    val text = count.toString()
    val digits = text.length
    val fontSize = when {
        digits <= 1 -> 10.sp
        digits == 2 -> 9.sp
        digits == 3 -> 8.sp
        else -> 7.sp
    }
    val shape = RoundedCornerShape(50)
    Box(
        modifier = modifier
            .height(minSize)
            .defaultMinSize(minWidth = minSize)
            .clip(shape)
            .background(backgroundColor)
            .then(
                if (borderColor != null)
                    Modifier.border(1.dp, borderColor, shape)
                else Modifier
            )
            .padding(horizontal = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = fontSize,
            lineHeight = fontSize,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

