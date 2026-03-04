package ngo.friendship.mhealth.dc.presentation.screens.main.case.components.case_detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ngo.friendship.mhealth.dc.theme.DarkerGray
import ngo.friendship.mhealth.dc.theme.Gray
import ngo.friendship.mhealth.dc.theme.GrayLighter
import ngo.friendship.mhealth.dc.theme.Resources
import ngo.friendship.mhealth.dc.theme.TextPrimary
import ngo.friendship.mhealth.dc.theme.UnfocusedBorderColor
import org.jetbrains.compose.resources.painterResource

@Composable
fun PrescriptionActionRowAligned(
    doseValue: String, doseItems: List<String>, onDoseSelect: (String) -> Unit,

    daysValue: String, daysItems: List<String>, onDaysSelect: (String) -> Unit,

    toggleValue: MealTime, onToggleChange: (MealTime) -> Unit,

    onMessageClick: () -> Unit, onAddClick: () -> Unit,

    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth().height(48.dp).padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            UnderlineDropdownMini(
                value = doseValue, items = doseItems, onSelect = onDoseSelect, width =64.dp
            )

            Spacer(Modifier.width(9.dp))

            UnderlineDropdownMini(
                value = daysValue, items = daysItems, onSelect = onDaysSelect, width = 64.dp
            )

            Spacer(Modifier.width(9.dp))

            AgePoreToggle(
                value = toggleValue, onChange = onToggleChange
            )
        }

        Spacer(Modifier.width(width = 9.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onMessageClick, modifier = Modifier.size(size = 34.dp)
            ) {
                Icon(
                    painter = painterResource(resource = Resources.Icon.Chat),
                    contentDescription = "Message",
                    tint = Gray,
                    modifier = Modifier.size(size = 28.dp).padding(all = 2.dp)
                )
            }

            Spacer(Modifier.width(width = 9.dp))

            AddMini(
                onClick = onAddClick
            )
        }
    }
}

@Composable
fun AgePoreToggle(
    value: MealTime, onChange: (MealTime) -> Unit, modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.height(height = 28.dp).clip(shape = RoundedCornerShape(size = 14.dp)).clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = true)
            ) {
                onChange(
                    if (value == MealTime.AGE) MealTime.PORE else MealTime.AGE
                )
            },
        shape = RoundedCornerShape(size = 14.dp),
        color = Color.White,
        border = BorderStroke(width = 1.dp, UnfocusedBorderColor),
        tonalElevation = 0.dp,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Restaurant,
                contentDescription = null,
                tint = Color(0xFF6B7280),
                modifier = Modifier.size(14.dp)
            )

            Spacer(Modifier.width(6.dp))

            Text(
                text = if (value == MealTime.AGE) "আগে" else "পরে",
                fontSize = 12.sp,
                color = TextPrimary,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun AddMini(
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.width(46.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(32.dp).clip(CircleShape).background(Color(0xFF1D4ED8))
                .clickable { onClick() }, contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Check,
                contentDescription = "Add",
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
fun UnderlineDropdownMini(
    value: String,
    items: List<String>,
    onSelect: (String) -> Unit,
    width: Dp,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    val finalModifier = if (modifier == Modifier) Modifier.width(width)
    else modifier

    Box(modifier = finalModifier) {

        Column(
            modifier = Modifier.fillMaxWidth().clickable { expanded = true }) {

            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = value,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal,
                    color = Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = Icons.Outlined.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Gray,
                    modifier = Modifier.size(14.dp)
                )
            }

            Box(
                modifier = Modifier.fillMaxWidth().height(1.dp).background(Gray)
            )
        }

        DropdownMenu(
            expanded = expanded, onDismissRequest = { expanded = false }) {
            items.forEach { item ->
                DropdownMenuItem(text = {
                    Text(
                        text = item, fontSize = 13.sp, color = DarkerGray
                    )
                }, onClick = {
                    onSelect(item)
                    expanded = false
                })
            }
        }
    }
}
