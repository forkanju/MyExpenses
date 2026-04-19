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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ngo.friendship.mhealth.dc.domain.model.Interview
import ngo.friendship.mhealth.dc.presentation.components.SearchRow
import ngo.friendship.mhealth.dc.presentation.components.TopTabsRow
import ngo.friendship.mhealth.dc.presentation.screens.case.case_list.components.CaseItem
import ngo.friendship.mhealth.dc.presentation.screens.case.case_list.components.CaseTab
import ngo.friendship.mhealth.dc.theme.FriendshipTheme

@Composable
fun CaseListScreen(
    modifier: Modifier = Modifier,
    interviewList: List<Interview>,
    selectedTab: CaseTab,
    tabCounts: Map<CaseTab, Int>,
    onTabSelect: (CaseTab) -> Unit,
    onCaseClick: (Interview) -> Unit = {},
    onFilterClick: () -> Unit = {},
) {
    var query by remember { mutableStateOf("") }

    val tabItems = CaseTab.entries.map { tab ->
        tab to (tabCounts[tab] ?: 0)
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
                        selected = selectedTab,
                        onSelect = onTabSelect
                    )

                    SearchRow(
                        query = query,
                        onQueryChange = { query = it },
                        onFilterClick = onFilterClick,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }
        }
    ) { padding ->
        val filteredList = if (query.isBlank()) {
            interviewList
        } else {
            interviewList.filter { item ->
                item.beneficiaryName.contains(query, ignoreCase = true) ||
                        item.beneficiaryCode.contains(query, ignoreCase = true) ||
                        item.location.contains(query, ignoreCase = true) ||
                        item.questionnaireName.contains(query, ignoreCase = true)
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(filteredList, key = { it.interviewId }) { item ->
                CaseItem(
                    ui = item,
                    onClick = { onCaseClick(item) },
                    isAnsweredStyle = selectedTab == CaseTab.Answered
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CaseListScreenPreview() {
    FriendshipTheme {
        CaseListScreen(
            interviewList = listOf(
                Interview(
                    interviewId = 1,
                    beneficiaryName = "John Doe",
                    beneficiaryCode = "B-12345",
                    location = "Dhaka, Bangladesh",
                    status = "Pending",
                    startTime = "2023-10-27 10:00:00",
                    questionnaireName = "General Health Checkup",
                    userName = "Dr. Smith"
                ),
                Interview(
                    interviewId = 2,
                    beneficiaryName = "Jane Doe",
                    beneficiaryCode = "B-67890",
                    location = "Chittagong, Bangladesh",
                    status = "Completed",
                    startTime = "2023-10-27 11:30:00",
                    questionnaireName = "Follow-up Visit",
                    userName = "Dr. Taylor"
                )
            ),
            selectedTab = CaseTab.Pending,
            tabCounts = mapOf(
                CaseTab.Pending to 2,
                CaseTab.Opened to 5,
                CaseTab.Older to 1,
                CaseTab.Answered to 9
            ),
            onTabSelect = {}
        )
    }
}