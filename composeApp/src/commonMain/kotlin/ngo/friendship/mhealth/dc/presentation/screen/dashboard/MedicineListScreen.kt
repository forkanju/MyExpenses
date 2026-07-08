package ngo.friendship.mhealth.dc.presentation.screen.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ngo.friendship.mhealth.dc.presentation.base.ObserveAsEvents
import ngo.friendship.mhealth.dc.presentation.base.SnackbarController
import ngo.friendship.mhealth.dc.presentation.base.SnackbarType
import ngo.friendship.mhealth.dc.presentation.components.CommonTopBar
import ngo.friendship.mhealth.dc.presentation.screen.dashboard.components.NewMedicineDialog
import ngo.friendship.mhealth.dc.theme.PrimaryBlue

@Composable
fun MedicineListScreen(
    viewModel: MedicineListViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    ObserveAsEvents(flow = viewModel.effect) { effect ->
        when (effect) {
            is MedicineListEffect.ShowSnackbar -> {
                if (effect.type == SnackbarType.SUCCESS) {
                    SnackbarController.sendEvent(message = effect.message, type = effect.type)
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                CommonTopBar(
                    title = "Medicine List",
                    onBack = onBack,
                    showSearch = true,
                    searchQuery = state.searchQuery,
                    onSearchQueryChange = {
                        viewModel.onIntent(MedicineListIntent.SearchQueryChanged(query = it))
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        viewModel.onIntent(MedicineListIntent.ToggleNewMedicineDialog)
                    },
                    containerColor = PrimaryBlue,
                    contentColor = Color.White,
                    shape = CircleShape,
                    modifier = Modifier.padding(bottom = 24.dp, end = 24.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                }
            }
        ) { paddingValues ->
            PullToRefreshBox(
                isRefreshing = state.isRefreshing,
                onRefresh = { viewModel.onIntent(MedicineListIntent.Refresh) },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(state.filteredMedicines) { item ->
                                MedicineListItem(
                                    title = "${item.type} : ${item.genericName}",
                                    subtitle = item.brandName
                                )
                                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                            }
                        }
                    }
                }
            }
        }

        if (state.showNewMedicineDialog) {
            NewMedicineDialog(
                itemTypes = state.medicineTypes,
                onDismiss = { viewModel.onIntent(MedicineListIntent.ToggleNewMedicineDialog) },
                onCreate = { type, generic, _, _ ->
                    viewModel.onIntent(MedicineListIntent.SaveMedicine(type, generic))
                }
            )
        }
    }
}

@Composable
fun MedicineListItem(title: String, subtitle: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        Text(text = title, fontSize = 16.sp, color = Color.Gray)
        Text(text = subtitle, fontSize = 12.sp, color = Color.LightGray)
    }
}

@Preview(showBackground = true)
@Composable
fun MedicineListScreenPreview() {
    // FriendshipTheme {
    //    MedicineListScreen(onBack = {})
    // }
}
