package ngo.friendship.mhealth.dc.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ngo.friendship.mhealth.dc.theme.Resources
import ngo.friendship.mhealth.dc.theme.RobotoCondensedFont
import ngo.friendship.mhealth.dc.theme.TextDarkerGray
import ngo.friendship.mhealth.dc.theme.TextSecondary
import org.jetbrains.compose.resources.painterResource

@Composable
fun SegmentedTabs(
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    leftText: String,
    rightText: String,
    isAnsweredMode: Boolean = false
) {
    Row {
        SegTab(
            text = leftText,
            selected = selectedIndex == 0,
            onClick = { onSelect(0) },
            isAnsweredMode = isAnsweredMode
        )
        Spacer(modifier = Modifier.width(6.dp))
        SegTab(
            text = rightText,
            selected = selectedIndex == 1,
            onClick = { onSelect(1) },
            isAnsweredMode = isAnsweredMode
        )
    }
}

@Composable
private fun SegTab(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    isAnsweredMode: Boolean = false
) {
    val selectedBg = if (isAnsweredMode) Color(0xFF8A8A8A) else Color(0xFF2F5EA8)
    val unselectedText = if (isAnsweredMode) Color(0xFF666666) else Color(0xFF5F6368)

    Box(
        modifier = Modifier
            .background(if (selected) selectedBg else Color.Transparent)
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 13.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            fontFamily = RobotoCondensedFont(),
            color = if (selected) Color.White else unselectedText
        )
    }
}

data class QAItem(
    val question: String,
    val answer: String
)

@Composable
fun ExpandableInterviewSummary(
    modifier: Modifier = Modifier,
    interviewItems: List<QAItem>,
    prescriptionItems: List<QAItem>,
    uploadedDateTime: String,
    selectedTab: Int,
    onTabSelect: (Int) -> Unit,
    onCopyClick: () -> Unit,
    onSeeFullClick: () -> Unit,
    expandedInitially: Boolean = true,
    isAnsweredMode: Boolean = false
) {
    var expanded by remember { mutableStateOf(expandedInitially) }

    val arrowRotation by animateFloatAsState(
        targetValue = if (expanded) 0f else 180f,
        label = "arrow"
    )

    val currentItems = remember(selectedTab, interviewItems, prescriptionItems, isAnsweredMode) {
        val items = if (selectedTab == 0) interviewItems else prescriptionItems
        items.filter {
            val normalizedAnswer = it.answer.replace("\\s+".toRegex(), "")
            val normalizedQuestion = it.question.replace("\\s+".toRegex(), "")
            val isHiddenData = normalizedAnswer == "Prescription28" || normalizedQuestion == "Prescription2_8" || normalizedQuestion == "Prescription" || normalizedQuestion == "Prescription28"
            if (isHiddenData) return@filter false
            
            if (isAnsweredMode) {
                it.answer.isNotBlank() && it.answer != "0" && it.answer != "null"
            } else {
                true
            }
        }
    }

    val cardColor = if (isAnsweredMode) Color(0xFFF5F5F5) else Color.White
    val topBackground = if (isAnsweredMode) Color(0xFFF5F5F5) else Color.White
    val footerColor = if (isAnsweredMode) Color(0xFF8A8A8A) else Color(0xFF2F5EA8)
    val dividerColor = if (isAnsweredMode) Color(0xFFD8D8D8) else Color(0xFFE7E7E7)
    val questionColor = if (isAnsweredMode) Color(0xFF7A7A7A) else TextSecondary
    val answerColor = if (isAnsweredMode) Color(0xFF535353) else TextDarkerGray
    val bulletColor = if (isAnsweredMode) Color(0xFF535353) else Color(0xFF2B2B2B)

    val grayFilter = if (isAnsweredMode) {
        ColorFilter.colorMatrix(
            ColorMatrix().apply { setToSaturation(0f) }
        )
    } else null

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            tonalElevation = 2.dp,
            shadowElevation = 4.dp,
            color = topBackground
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(topBackground),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SegmentedTabs(
                    selectedIndex = selectedTab,
                    onSelect = onTabSelect,
                    leftText = "Interview Summary",
                    rightText = "System Prescription",
                    isAnsweredMode = isAnsweredMode
                )

                Spacer(modifier = Modifier.weight(1f))

                Box(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(28.dp)
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    Image(
                        painter = painterResource(Resources.Icon.Symptom),
                        contentDescription = "Symptom",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        colorFilter = grayFilter
                    )
                    Image(
                        painter = painterResource(Resources.Icon.Copy),
                        contentDescription = "Copy",
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(4.dp)
                            .size(22.dp)
                            .clickable { onCopyClick() },
                        colorFilter = grayFilter
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 220.dp)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                if (currentItems.isEmpty()) {
                    Text(
                        text = if (selectedTab == 0) {
                            "No interview summary found"
                        } else {
                            "No system prescription found"
                        },
                        fontSize = 14.sp,
                        color = answerColor,
                        fontFamily = RobotoCondensedFont()
                    )
                } else {
                    currentItems.forEachIndexed { index, qa ->
                        Text(
                            text = qa.question.replace("_", " "),
                            fontSize = 13.sp,
                            color = questionColor,
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.Normal,
                            fontFamily = RobotoCondensedFont()
                        )

                        Spacer(modifier = Modifier.height(1.dp))

                        Row(verticalAlignment = Alignment.Top) {
                            Text(
                                text = "•",
                                fontSize = 16.sp,
                                fontFamily = RobotoCondensedFont(),
                                color = bulletColor
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = qa.answer.replace("_", " "),
                                fontSize = 14.sp,
                                color = answerColor,
                                fontFamily = RobotoCondensedFont()
                            )
                        }

                        if (index != currentItems.lastIndex) {
                            Spacer(modifier = Modifier.height(3.dp))
                            HorizontalDivider(
                                color = dividerColor,
                                thickness = 1.dp
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp)
                .background(footerColor)
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(resource = Resources.Icon.Arrow),
                contentDescription = "Expand/Collapse",
                tint = Color.White,
                modifier = Modifier
                    .size(14.dp)
                    .rotate(arrowRotation)
                    .clickable { expanded = !expanded }
            )

            Text(
                text = "",//See full Interview
                fontSize = 13.sp,
                color = Color.White,
                textDecoration = TextDecoration.Underline,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Normal,
                fontFamily = RobotoCondensedFont(),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clickable { onSeeFullClick() }
            )
        }
    }
}