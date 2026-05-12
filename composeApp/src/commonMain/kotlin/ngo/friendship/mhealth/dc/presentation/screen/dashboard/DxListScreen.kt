package ngo.friendship.mhealth.dc.presentation.screens.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ngo.friendship.mhealth.dc.presentation.components.CommonTopBar
import ngo.friendship.mhealth.dc.presentation.screen.dashboard.DxListViewModel
import ngo.friendship.mhealth.dc.presentation.screens.dashboard.components.CommonNewItemDialog
import ngo.friendship.mhealth.dc.theme.FriendshipTheme
import ngo.friendship.mhealth.dc.theme.PrimaryBlue

@Preview(showBackground = true)
@Composable
fun DxListPreview() {
    val sampleDxItems = listOf(
        DxItemData(
            "ANC", "Updated: 1:16 PM, 25 Jan 25 Created: 3:35 PM, 12 Nov 25", listOf(
                "1. Found via glucose tolerance test; managed with diet/insulin.",
                "2. Positive tests for HIV, Hepatitis B, Syphilis, Rubella (needs management to protect baby)."
            ),
            isExpanded = true
        ),
        DxItemData("Fever", "Updated: 1:16 PM, 25 Jan 25 Created: 3:35 PM, 12 Nov 25"),
        DxItemData("Oral Ulcer RX", "Updated: 1:16 PM, 25 Jan 25 Created: 3:35 PM, 12 Nov 25")
    )

    FriendshipTheme {
        DxListContent(
            state = DxListState(
                dxItems = sampleDxItems,
                filteredDxItems = sampleDxItems
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onBack = {},
            onIntent = {}
        )
    }
}

@Composable
fun DxListScreen(
    viewModel: DxListViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                DxListEffect.DxCreated -> {
                    snackbarHostState.showSnackbar("Diagnosis saved successfully")
                }
            }
        }
    }

    LaunchedEffect(state.error) {
        state.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onIntent(DxListIntent.ClearError)
        }
    }

    DxListContent(
        state = state,
        snackbarHostState = snackbarHostState,
        onBack = onBack,
        onIntent = viewModel::onIntent
    )
}

@Composable
fun DxListContent(
    state: DxListState,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onBack: () -> Unit,
    onIntent: (DxListIntent) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                CommonTopBar(
                    title = "DX List",
                    onBack = onBack,
                    showSearch = true,
                    searchQuery = state.searchQuery,
                    onSearchQueryChange = { onIntent(DxListIntent.Search(it)) }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        onIntent(DxListIntent.ShowNewDxDialog(true))
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
                    LazyColumn {
                        items(state.filteredDxItems) { item ->
                            DxListItem(item = item)
                            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                        }
                    }
                }
            }
        }

        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }

        if (state.showNewDxDialog) {
            CommonNewItemDialog(
                dialogTitle = "New DX",
                titleLabel = "DX Title",
                contentLabel = "Advices",
                showTitleField = true,
                showContentField = false,
                onDismiss = { onIntent(DxListIntent.ShowNewDxDialog(false)) },
                onCreate = { title: String, content: String ->
                    onIntent(DxListIntent.CreateDx(title, content))
                }
            )
        }
    }
}

@Composable
fun DxListItem(
    item: DxItemData
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = item.title.replace("_", " "),
            fontSize = 16.sp,
            color = Color.Gray
        )
    }
}
