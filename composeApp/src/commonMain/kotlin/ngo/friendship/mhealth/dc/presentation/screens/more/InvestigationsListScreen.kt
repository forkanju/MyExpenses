package ngo.friendship.mhealth.dc.presentation.screens.more

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ngo.friendship.mhealth.dc.presentation.components.CommonTopBar
import ngo.friendship.mhealth.dc.theme.FriendshipTheme
import ngo.friendship.mhealth.dc.theme.PrimaryBlue

@Composable
fun InvestigationsListScreen(
    onBack: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    var selectedFilter by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }
    val filters = listOf("All", "Recent Used", "Recent Updated")
    
    val investigationItems = listOf(
        InvestigationItemData("ANC", "Updated: 1:16 PM, 25 Jan 25 Created: 3:35 PM, 12 Nov 25", listOf(
            "1. X- Ray",
            "2. CBC"
        )),
        InvestigationItemData("Fever", "Updated: 1:16 PM, 25 Jan 25 Created: 3:35 PM, 12 Nov 25"),
        InvestigationItemData("Oral Ulcer RX", "Updated: 1:16 PM, 25 Jan 25 Created: 3:35 PM, 12 Nov 25"),
        InvestigationItemData("UTI", "Updated: 1:16 PM, 25 Jan 25 Created: 3:35 PM, 12 Nov 25"),
        InvestigationItemData("Migraine RX", "Updated: 1:16 PM, 25 Jan 25 Created: 3:35 PM, 12 Nov 25")
    )

    Scaffold(
        modifier = Modifier.pointerInput(Unit) {
            detectTapGestures(onTap = {
                focusManager.clearFocus()
            })
        },
        topBar = {
            CommonTopBar(
                title = "Investigations List",
                onBack = onBack,
                showSearch = true,
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { },
                containerColor = PrimaryBlue,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.padding(bottom = 85.dp, end = 34.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { paddingValues ->
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    filters.forEach { filter ->
                        InvestigationFilterChip(
                            text = filter,
                            isSelected = selectedFilter == filter,
                            onClick = { selectedFilter = filter }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(investigationItems) { item ->
                        InvestigationExpandableItem(item)
                        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                    }
                }
            }
        }
    }
}

@Composable
fun InvestigationFilterChip(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(5.dp),
        color = if (isSelected) PrimaryBlue else Color.White,
        border = if (isSelected) null else BorderStroke(1.dp, Color.Gray)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 1.dp),
            fontSize = 12.sp,
            color = if (isSelected) Color.White else Color.Gray
        )
    }
}

data class InvestigationItemData(
    val title: String,
    val subtitle: String,
    val details: List<String> = emptyList()
)

@Composable
fun InvestigationExpandableItem(item: InvestigationItemData) {
    var expanded by remember { mutableStateOf(item.title == "ANC") }

    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f).padding(start = 16.dp)) {
                Text(text = item.title, fontSize = 16.sp, color = Color.Gray)
                Text(text = item.subtitle, fontSize = 10.sp, color = Color.LightGray)
            }
            Icon(
                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = "Expand",
                tint = Color.Gray
            )
        }
        
        AnimatedVisibility(visible = expanded) {
            Column(modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp)) {
                item.details.forEach { detail ->
                    Text(
                        text = detail,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InvestigationsListScreenPreview() {
    FriendshipTheme {
        InvestigationsListScreen(onBack = {})
    }
}
