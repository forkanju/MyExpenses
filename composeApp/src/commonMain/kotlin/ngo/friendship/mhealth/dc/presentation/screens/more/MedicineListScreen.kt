package ngo.friendship.mhealth.dc.presentation.screens.more

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ngo.friendship.mhealth.dc.presentation.MainViewModel
import ngo.friendship.mhealth.dc.presentation.navigation.Screens
import ngo.friendship.mhealth.dc.presentation.screens.more.components.NewMedicineDialog
import ngo.friendship.mhealth.dc.presentation.components.CommonTopBar
import ngo.friendship.mhealth.dc.theme.FriendshipTheme
import ngo.friendship.mhealth.dc.theme.PrimaryBlue

@Composable
fun MedicineListScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    var selectedFilter by remember { mutableStateOf("All") }
    val filters = listOf("All", "Recent Used", "Recent Updated")
    var searchQuery by remember { mutableStateOf("") }
    var showNewMedicineDialog by remember { mutableStateOf(false) }


    val medicineItems = listOf(
        MedicineItemData(
            "Pantoprazole 20 mg",
            "Updated: 1:16 PM, 25 Jan 25 Created: 3:35 PM, 12 Nov 25"
        ),
        MedicineItemData("Fexo 20 mg", "Updated: 1:16 PM, 25 Jan 25 Created: 3:35 PM, 12 Nov 25"),
        MedicineItemData(
            "Esomeprazole 20 mg",
            "Updated: 1:16 PM, 25 Jan 25 Created: 3:35 PM, 12 Nov 25"
        ),
        MedicineItemData(
            "Becozym 0.5 mg",
            "Updated: 1:16 PM, 25 Jan 25 Created: 3:35 PM, 12 Nov 25"
        ),
        MedicineItemData("Alatrol 10 mg", "Updated: 1:16 PM, 25 Jan 25 Created: 3:35 PM, 12 Nov 25")
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                CommonTopBar(
                    title = "Medicine List",
                    onBack = onBack,
                    showSearch = true,
                    searchQuery = searchQuery,
                    onSearchQueryChange = { searchQuery = it }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        showNewMedicineDialog = true
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
                        items(medicineItems) { item ->
                            MedicineListItem(item)
                            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                        }
                    }
                }
            }
        }

        if (showNewMedicineDialog) {
            NewMedicineDialog(
                onDismiss = { showNewMedicineDialog = false },
                onCreate = { type: String, generic: String, strength: String, brands: String ->
                    // TODO: Handle creation logic
                    showNewMedicineDialog = false
                }
            )
        }
    }
}


data class MedicineItemData(
    val title: String,
    val subtitle: String
)

@Composable
fun MedicineListItem(item: MedicineItemData) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        Text(text = item.title, fontSize = 16.sp, color = Color.Gray)
        Text(text = item.subtitle, fontSize = 10.sp, color = Color.LightGray)
    }
}

@Preview(showBackground = true)
@Composable
fun MedicineListScreenPreview() {
    // FriendshipTheme {
    //    MedicineListScreen(onBack = {})
    // }
}
