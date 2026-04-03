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
    selectedIndex: Int, onSelect: (Int) -> Unit, leftText: String, rightText: String
) {
    Row(
        modifier = Modifier
    ) {
        SegTab(text = leftText, selected = selectedIndex == 0) { onSelect(0) }
        Spacer(modifier = Modifier.width(6.dp))
        SegTab(text = rightText, selected = selectedIndex == 1) { onSelect(1) }
    }
}

@Composable
private fun SegTab(
    text: String, selected: Boolean, onClick: () -> Unit
) {
    Box(
        modifier = Modifier.background(if (selected) Color(0xFF2F5EA8) else Color.Transparent)
            .clickable { onClick() }.padding(horizontal = 10.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 13.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            fontFamily = RobotoCondensedFont(),
            color = if (selected) Color.White else Color(0xFF5F6368)
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
    uploadedText: String,
    selectedTab: Int,
    onTabSelect: (Int) -> Unit,
    onCopyClick: () -> Unit,
    onSeeFullClick: () -> Unit,
    expandedInitially: Boolean = true
) {
    var expanded by remember { mutableStateOf(expandedInitially) }

    val arrowRotation by animateFloatAsState(
        targetValue = if (expanded) 0f else 180f,
        label = "arrow"
    )

    val currentItems = remember(selectedTab, interviewItems, prescriptionItems) {
        if (selectedTab == 0) interviewItems else prescriptionItems
    }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            tonalElevation = 2.dp,
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SegmentedTabs(
                    selectedIndex = selectedTab,
                    onSelect = onTabSelect,
                    leftText = "Interview Summary",
                    rightText = "System Prescription"
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
                        contentScale = ContentScale.Crop
                    )
                    Image(
                        painter = painterResource(Resources.Icon.Copy),
                        contentDescription = "Copy",
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(4.dp)
                            .size(22.dp)
                            .clickable { onCopyClick() }
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
                        color = TextDarkerGray,
                        fontFamily = RobotoCondensedFont()
                    )
                } else {
                    currentItems.forEachIndexed { index, qa ->
                        Text(
                            text = qa.question,
                            fontSize = 13.sp,
                            color = TextSecondary,
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.Normal,
                            fontFamily = RobotoCondensedFont()
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(verticalAlignment = Alignment.Top) {
                            Text(
                                text = "•",
                                fontSize = 16.sp,
                                fontFamily = RobotoCondensedFont(),
                                color = Color(0xFF2B2B2B)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = qa.answer,
                                fontSize = 14.sp,
                                color = TextDarkerGray,
                                fontFamily = RobotoCondensedFont()
                            )
                        }

                        if (index != currentItems.lastIndex) {
                            Spacer(modifier = Modifier.height(10.dp))
                            HorizontalDivider(
                                color = Color(0xFFE7E7E7),
                                thickness = 1.dp
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp)
                .background(Color(0xFF2F5EA8))
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Uploaded: ",
                fontSize = 12.sp,
                color = Color.White,
                fontStyle = FontStyle.Italic,
                fontFamily = RobotoCondensedFont()
            )

            Text(
                text = uploadedText,
                fontSize = 12.sp,
                color = Color.White,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                fontFamily = RobotoCondensedFont()
            )

            Spacer(Modifier.width(6.dp))

            Icon(
                painter = painterResource(resource = Resources.Icon.Arrow),
                contentDescription = "Expand/Collapse",
                tint = Color.White,
                modifier = Modifier
                    .size(14.dp)
                    .rotate(arrowRotation)
                    .clickable { expanded = !expanded }
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "See full Interview",
                fontSize = 13.sp,
                color = Color.White,
                textDecoration = TextDecoration.Underline,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Normal,
                fontFamily = RobotoCondensedFont(),
                modifier = Modifier.clickable { onSeeFullClick() }
            )
        }
    }
}