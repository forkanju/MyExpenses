package ngo.friendship.mhealth.dc.presentation.screen.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ngo.friendship.mhealth.dc.presentation.MainViewModel
import ngo.friendship.mhealth.dc.presentation.screen.dashboard.components.CommonNewItemDialog
import ngo.friendship.mhealth.dc.presentation.components.CommonTopBar
import ngo.friendship.mhealth.dc.theme.PrimaryBlue

@Composable
fun InvestigationsListScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    var searchQuery by remember { mutableStateOf("") }
    var showNewInvestigationDialog by remember { mutableStateOf(false) }
    
    val setupData by viewModel.setupDataState.collectAsState()
    val isLoading by viewModel.loadingFlow.collectAsState()
    val isRefreshing by viewModel.loadingSecondaryFlow.collectAsStateWithLifecycle()

    val investigationItems = setupData.investigations.filter {
        it.investigationName.contains(searchQuery, ignoreCase = true)
    }

    Box(modifier = Modifier.fillMaxSize()) {
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
                    onClick = {
                        showNewInvestigationDialog = true
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
            PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = { viewModel.refreshSetupData() },
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
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(investigationItems) { item ->
                                InvestigationItem(item.investigationName)
                                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                            }
                        }
                    }
                }
            }
        }

        if (showNewInvestigationDialog) {
            CommonNewItemDialog(
                dialogTitle = "New Investigation",
                titleLabel = "Investigation title",
                showContentField = false,
                onDismiss = { showNewInvestigationDialog = false },
                onCreate = { title: String, _: String ->
                    viewModel.saveInvestigation(title)
                    showNewInvestigationDialog = false
                }
            )
        }

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = PrimaryBlue)
            }
        }
    }
}

@Composable
fun InvestigationItem(title: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp, horizontal = 16.dp)) {
        Text(text = title, fontSize = 16.sp, color = Color.Gray)
    }
}

@Preview(showBackground = true)
@Composable
fun InvestigationsListScreenPreview() {
    // FriendshipTheme {
    //    InvestigationsListScreen(onBack = {})
    // }
}
