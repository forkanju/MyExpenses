package ngo.friendship.mhealth.dc.presentation.screens.case.case_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collectLatest
import ngo.friendship.mhealth.dc.domain.model.Interview
import ngo.friendship.mhealth.dc.presentation.components.SearchRow
import ngo.friendship.mhealth.dc.presentation.components.TopTabsRow
import ngo.friendship.mhealth.dc.presentation.screens.case.case_list.components.CaseItem
import ngo.friendship.mhealth.dc.presentation.screens.case.case_list.components.CaseTab
import ngo.friendship.mhealth.dc.theme.FriendshipTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CaseListScreen(
    modifier: Modifier = Modifier,
    viewModel: CaseListViewModel = koinViewModel(),
    onNavigateToDetails: (Interview, CaseTab) -> Unit = { _, _ -> }
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is CaseListEffect.NavigateToDetails -> onNavigateToDetails(
                    effect.interview,
                    effect.sourceTab
                )

                CaseListEffect.OpenFilterSheet -> {
                    // Handle filter sheet
                }
            }
        }
    }

    CaseListContent(
        modifier = modifier,
        state = state,
        onIntent = viewModel::onIntent
    )
}

@Composable
fun CaseListContent(
    modifier: Modifier = Modifier,
    state: CaseListState,
    onIntent: (CaseListIntent) -> Unit
) {
    val tabItems = CaseTab.entries.map { tab ->
        tab to (state.tabCounts[tab] ?: 0)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color(0xFFF3F3F3),
        topBar = {
            Surface(
                color = Color(0xFFF3F3F3),
                shadowElevation = 0.dp
            ) {
                Column(Modifier.fillMaxWidth()) {
                    TopTabsRow(
                        tabs = tabItems,
                        selected = state.selectedTab,
                        onSelect = { onIntent(CaseListIntent.SelectTab(it)) }
                    )

                    SearchRow(
                        query = state.searchQuery,
                        onQueryChange = { onIntent(CaseListIntent.Search(it)) },
                        onFilterClick = { onIntent(CaseListIntent.ClickFilter) },
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(state.filteredInterviews, key = { it.interviewId }) { item ->
                CaseItem(
                    ui = item,
                    onClick = { onIntent(CaseListIntent.ClickCase(item)) },
                    isAnsweredStyle = state.selectedTab == CaseTab.Answered
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CaseListScreenPreview() {
    FriendshipTheme {
        CaseListContent(
            state = CaseListState(
                interviews = listOf(
                    Interview(
                        interviewId = 1,
                        beneficiaryName = "John Doe",
                        beneficiaryCode = "B-12345",
                        location = "Dhaka, Bangladesh",
                        status = "Pending",
                        startTime = "2023-10-27 10:00:00",
                        questionnaireName = "General Health Checkup",
                        userName = "Dr. Smith"
                    )
                ),
                filteredInterviews = listOf(
                    Interview(
                        interviewId = 1,
                        beneficiaryName = "John Doe",
                        beneficiaryCode = "B-12345",
                        location = "Dhaka, Bangladesh",
                        status = "Pending",
                        startTime = "2023-10-27 10:00:00",
                        questionnaireName = "General Health Checkup",
                        userName = "Dr. Smith"
                    )
                ),
                tabCounts = mapOf(CaseTab.Pending to 1)
            ),
            onIntent = {}
        )
    }
}
