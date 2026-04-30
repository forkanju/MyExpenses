package ngo.friendship.mhealth.dc.presentation.screens.dashboard

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
import ngo.friendship.mhealth.dc.presentation.MainViewModel
import ngo.friendship.mhealth.dc.presentation.navigation.Screens
import ngo.friendship.mhealth.dc.presentation.screens.dashboard.components.CommonFilterChip
import ngo.friendship.mhealth.dc.presentation.screens.dashboard.components.CommonNewItemDialog
import ngo.friendship.mhealth.dc.theme.FriendshipTheme
import ngo.friendship.mhealth.dc.theme.PrimaryBlue

@Composable
fun AdviceTemplateListScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    var selectedFilter by remember { mutableStateOf("All") }
    val filters = listOf("All", "Recent Used", "Recent Updated")
    var searchQuery by remember { mutableStateOf("") }
    var showNewAdviceDialog by remember { mutableStateOf(false) }

    val adviceItems = listOf(
        AdviceItemData(
            "ANC", "Updated: 1:16 PM, 25 Jan 25 Created: 3:35 PM, 12 Nov 25", listOf(
                "১. প্রতিদিন ২-৩ লিটার পানি ......",
                "২. প্রসাব আটকে রাখবেন না, ......",
                "৩. সহবাস করার আগে অবশ্যই ......",
                "৪. পরিষ্কার পরিচ্ছন্নতা বজায় ......",
                "৫. প্রসাব করার সময় পুরা প্রসাব......"
            )
        ),
        AdviceItemData("Fever", "Updated: 1:16 PM, 25 Jan 25 Created: 3:35 PM, 12 Nov 25"),
        AdviceItemData("Oral Ulcer RX", "Updated: 1:16 PM, 25 Jan 25 Created: 3:35 PM, 12 Nov 25"),
        AdviceItemData("UTI", "Updated: 1:16 PM, 25 Jan 25 Created: 3:35 PM, 12 Nov 25"),
        AdviceItemData("Migraine RX", "Updated: 1:16 PM, 25 Jan 25 Created: 3:35 PM, 12 Nov 25")
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                CommonTopBar(
                    title = "Advice Templates",
                    onBack = onBack,
                    showSearch = true,
                    searchQuery = searchQuery,
                    onSearchQueryChange = { searchQuery = it }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        showNewAdviceDialog = true
                    },
                    containerColor = PrimaryBlue,
                    contentColor = Color.White,
                    shape = CircleShape,
                    modifier = Modifier.padding(bottom = 24.dp, end = 24.dp)
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
                            CommonFilterChip(
                                text = filter,
                                isSelected = selectedFilter == filter,
                                onClick = { selectedFilter = filter }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(adviceItems) { item ->
                            AdviceExpandableItem(item)
                            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                        }
                    }
                }
            }
        }

        if (showNewAdviceDialog) {
            CommonNewItemDialog(
                dialogTitle = "New Advice",
                titleLabel = "Advice Title",
                contentLabel = "Advices",
                onDismiss = { showNewAdviceDialog = false },
                onCreate = { title: String, content: String ->
                    // TODO: Handle creation logic
                    showNewAdviceDialog = false
                }
            )
        }
    }
}

data class AdviceItemData(
    val title: String,
    val subtitle: String,
    val details: List<String> = emptyList()
)

@Composable
fun AdviceExpandableItem(item: AdviceItemData) {
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
fun AdviceTemplateListScreenPreview() {
    // FriendshipTheme {
    //    AdviceTemplateListScreen(onBack = {})
    // }
}
