package ngo.friendship.mhealth.dc.presentation.screens.main.case

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ngo.friendship.mhealth.dc.domain.model.Interview
import ngo.friendship.mhealth.dc.presentation.screens.main.case.InterviewListViewModel
import ngo.friendship.mhealth.dc.presentation.screens.main.case.components.CaseListItem
import ngo.friendship.mhealth.dc.presentation.screens.main.case.components.CaseTab
import ngo.friendship.mhealth.dc.presentation.screens.main.case.components.SearchRow
import ngo.friendship.mhealth.dc.presentation.screens.main.case.components.TopTabsRow

@Composable
fun CaseScreen(
    modifier: Modifier = Modifier,
    interviewVm: InterviewListViewModel,
    interviewList: List<Interview>,
    onCaseClick: (Interview) -> Unit = {},
    onFilterClick: () -> Unit = {},
) {
    var selectedTab by remember { mutableStateOf(CaseTab.Pending) }
    var query by remember { mutableStateOf("") }

    //First time load
    LaunchedEffect(Unit) {
        interviewVm.loadInterviewList(
            userName = "sharif.dr",
            password = "1234",
            appVersion = 3069
        )
    }
    //domain - ui mapping
    val cases = remember(interviewList) {
        interviewList.map { it }
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
                        // This now uses the correct Enum reference
                        tabs = CaseTab.entries.map { it to 10 },
                        selected = selectedTab,
                        onSelect = { selectedTab = it }
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
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(cases, key = { it.interviewId }) { item ->
                CaseListItem(ui = item, onClick = { onCaseClick(item) })
            }
        }
    }
}