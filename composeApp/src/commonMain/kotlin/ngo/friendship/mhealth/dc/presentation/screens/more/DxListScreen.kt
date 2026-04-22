package ngo.friendship.mhealth.dc.presentation.screens.more

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ngo.friendship.mhealth.dc.presentation.components.CommonTopBar
import ngo.friendship.mhealth.dc.presentation.components.FilterChip
import ngo.friendship.mhealth.dc.presentation.screens.more.components.NewDxDialog
import ngo.friendship.mhealth.dc.theme.FriendshipTheme
import ngo.friendship.mhealth.dc.theme.PrimaryBlue

@Composable
fun DxListScreen(
    onBack: () -> Unit
) {
    var selectedFilter by remember { mutableStateOf("All") }
    val filters = listOf("All", "Recent Used", "Recent Updated")

    var showNewDxDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }


    val dxItems = listOf(
        DxItemData(
            "ANC", "Updated: 1:16 PM, 25 Jan 25 Created: 3:35 PM, 12 Nov 25", listOf(
                "1. Found via glucose tolerance test; managed with diet/insulin.",
                "2. Positive tests for HIV, Hepatitis B, Syphilis, Rubella (needs management to protect baby)."
            )
        ),
        DxItemData("Fever", "Updated: 1:16 PM, 25 Jan 25 Created: 3:35 PM, 12 Nov 25"),
        DxItemData("Oral Ulcer RX", "Updated: 1:16 PM, 25 Jan 25 Created: 3:35 PM, 12 Nov 25"),
        DxItemData("UTI", "Updated: 1:16 PM, 25 Jan 25 Created: 3:35 PM, 12 Nov 25"),
        DxItemData("Migraine RX", "Updated: 1:16 PM, 25 Jan 25 Created: 3:35 PM, 12 Nov 25")
    )

    Scaffold(
        topBar = {
            CommonTopBar(
                title = "DX List",
                onBack = onBack,
                showSearch = true,
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showNewDxDialog = true },
                containerColor = PrimaryBlue,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.padding(bottom = 40.dp, end = 16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
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
                            FilterChip(
                                text = filter,
                                isSelected = selectedFilter == filter,
                                onClick = { selectedFilter = filter }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(dxItems) { item ->
                            DxExpandableItem(item)
                            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                        }
                    }
                }
            }

            if (showNewDxDialog) {
                NewDxDialog(
                    onDismiss = { showNewDxDialog = false },
                    onCreate = { title, advice ->
                        // Handle creation logic here
                        showNewDxDialog = false
                    }
                )
            }
        }
    }
}


data class DxItemData(
    val title: String,
    val subtitle: String,
    val details: List<String> = emptyList()
)

@Composable
fun DxExpandableItem(item: DxItemData) {
    var expanded by remember { mutableStateOf(item.title == "ANC") } // Default ANC expanded as per screenshot

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
fun DxListScreenPreview() {
    FriendshipTheme {
        DxListScreen(onBack = {})
    }
}
