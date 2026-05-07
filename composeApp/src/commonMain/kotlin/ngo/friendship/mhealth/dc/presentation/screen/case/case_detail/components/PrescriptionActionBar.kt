package ngo.friendship.mhealth.dc.presentation.screens.case.case_detail.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material3.*
import androidx.compose.material3.ripple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import ngo.friendship.mhealth.dc.theme.*

@Composable
fun PrescriptionActionRowAligned(
    doseValue: String,
    doseItems: List<String>,
    onDoseSelect: (String) -> Unit,

    daysValue: String,
    daysItems: List<String>,
    onDaysSelect: (String) -> Unit,

    toggleValue: ngo.friendship.mhealth.dc.presentation.screen.case.case_detail.components.MealTime,
    onToggleChange: (ngo.friendship.mhealth.dc.presentation.screen.case.case_detail.components.MealTime) -> Unit,

    onMessageClick: () -> Unit,
    onAddClick: () -> Unit,

    modifier: Modifier = Modifier,
    isAnsweredMode: Boolean = false
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {

            UnderlineDropdownMini(
                value = doseValue,
                items = doseItems,
                onSelect = onDoseSelect,
                width = 64.dp,
                isAnsweredMode = isAnsweredMode
            )

            Spacer(Modifier.width(9.dp))

            UnderlineDropdownMini(
                value = daysValue,
                items = daysItems,
                onSelect = onDaysSelect,
                width = 64.dp,
                isAnsweredMode = isAnsweredMode
            )

            Spacer(Modifier.width(9.dp))

            AgePoreToggle(
                value = toggleValue,
                onChange = onToggleChange,
                isAnsweredMode = isAnsweredMode
            )
        }

        Spacer(Modifier.width(9.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {

            Spacer(Modifier.width(9.dp))

            AddMini(
                onClick = onAddClick,
                isAnsweredMode = isAnsweredMode
            )
        }
    }
}

@Composable
fun AgePoreToggle(
    value: ngo.friendship.mhealth.dc.presentation.screen.case.case_detail.components.MealTime,
    onChange: (ngo.friendship.mhealth.dc.presentation.screen.case.case_detail.components.MealTime) -> Unit,
    modifier: Modifier = Modifier,
    isAnsweredMode: Boolean = false
) {
    val bgColor = if (isAnsweredMode) Color(0xFFF7F7F7) else Color.White
    val borderColor = if (isAnsweredMode) Color(0xFFC7C7C7) else UnfocusedBorderColor
    val textColor = if (isAnsweredMode) Color(0xFF4F4F4F) else TextPrimary
    val iconColor = if (isAnsweredMode) Color(0xFF6A6A6A) else Color(0xFF6B7280)

    Surface(
        modifier = modifier
            .height(28.dp)
            .clip(RoundedCornerShape(14.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple()
            ) {
                onChange(
                    if (value == _root_ide_package_.ngo.friendship.mhealth.dc.presentation.screen.case.case_detail.components.MealTime.AGE) _root_ide_package_.ngo.friendship.mhealth.dc.presentation.screen.case.case_detail.components.MealTime.PORE else _root_ide_package_.ngo.friendship.mhealth.dc.presentation.screen.case.case_detail.components.MealTime.AGE
                )
            },
        shape = RoundedCornerShape(14.dp),
        color = bgColor,
        border = BorderStroke(1.dp, borderColor)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Restaurant,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(14.dp)
            )

            Spacer(Modifier.width(6.dp))

            Text(
                text = if (value == _root_ide_package_.ngo.friendship.mhealth.dc.presentation.screen.case.case_detail.components.MealTime.AGE) "আগে" else "পরে",
                fontSize = 12.sp,
                color = textColor,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun AddMini(
    onClick: () -> Unit,
    isAnsweredMode: Boolean = false
) {
    val bgColor = if (isAnsweredMode) Color(0xFF9E9E9E) else Color(0xFF1D4ED8)

    Column(
        modifier = Modifier.width(46.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(bgColor)
                .clickable { onClick() },
            contentAlignment = Alignment.Center
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
    modifier: Modifier = Modifier,
    isAnsweredMode: Boolean = false
) {
    var expanded by remember { mutableStateOf(false) }

    val textColor = if (isAnsweredMode) Color(0xFF4F4F4F) else Gray
    val dividerColor = if (isAnsweredMode) Color(0xFFC7C7C7) else Gray
    val dropdownText = if (isAnsweredMode) Color(0xFF4F4F4F) else DarkerGray

    val finalModifier = if (modifier == Modifier) Modifier.width(width)
    else modifier

    Box(modifier = finalModifier) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = value,
                    fontSize = 13.sp,
                    color = textColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = Icons.Outlined.KeyboardArrowDown,
                    contentDescription = null,
                    tint = textColor,
                    modifier = Modifier.size(14.dp)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(dividerColor)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = if (isAnsweredMode) Color(0xFFF7F7F7) else Color.White
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = item,
                            fontSize = 13.sp,
                            color = dropdownText
                        )
                    },
                    onClick = {
                        onSelect(item)
                        expanded = false
                    }
                )
            }
        }
    }
}