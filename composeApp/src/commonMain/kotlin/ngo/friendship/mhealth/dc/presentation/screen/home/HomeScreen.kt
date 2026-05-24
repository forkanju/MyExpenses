package ngo.friendship.mhealth.dc.presentation.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ngo.friendship.mhealth.dc.presentation.screen.home.HomeViewModel
import ngo.friendship.mhealth.dc.presentation.screens.home.components.HomeTopRow
import ngo.friendship.mhealth.dc.presentation.screen.home.components.KeyValueListCard
import ngo.friendship.mhealth.dc.presentation.screens.home.components.SectionTitle
import ngo.friendship.mhealth.dc.presentation.screens.home.components.SegmentedBarCard
import ngo.friendship.mhealth.dc.presentation.screens.home.components.StatRingCard
import ngo.friendship.mhealth.dc.presentation.screens.home.components.TrendTableCard
import ngo.friendship.mhealth.dc.presentation.screen.home.components.TwoCardsRow
import ngo.friendship.mhealth.dc.presentation.screens.home.model.KeyValueUi
import ngo.friendship.mhealth.dc.presentation.screens.home.model.SegmentUi
import ngo.friendship.mhealth.dc.presentation.screens.home.model.StatRingUi
import ngo.friendship.mhealth.dc.presentation.screens.home.model.TrendRowUi
import ngo.friendship.mhealth.dc.theme.FriendshipTheme
import ngo.friendship.mhealth.dc.theme.RingBarBlue
import ngo.friendship.mhealth.dc.theme.RingBarGreen
import ngo.friendship.mhealth.dc.theme.RingBarRed
import ngo.friendship.mhealth.dc.theme.TrendBarRed
import ngo.friendship.mhealth.dc.theme.TrendBlue
import ngo.friendship.mhealth.dc.theme.TrendRed
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    PullToRefreshBox(
        isRefreshing = state.isRefreshing,
        onRefresh = { viewModel.onIntent(HomeIntent.Refresh) },
        modifier = modifier.fillMaxSize()
    ) {
        HomeScreenContent(
            modifier = Modifier.fillMaxSize(),
            title = state.title,
            totalCaseText = state.totalCaseText,
            stats = state.stats,
            trendRows = state.trendRows,
            segments = state.segments,
            byServices = state.byServices,
            byArea = state.byArea
        )
    }
}

@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    title: String,
    totalCaseText: String,
    stats: List<StatRingUi>,
    trendRows: List<TrendRowUi>,
    segments: List<SegmentUi>,
    byServices: List<KeyValueUi>,
    byArea: List<KeyValueUi>
) {
    LazyColumn(
        contentPadding = PaddingValues(all = 12.dp),
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            HomeTopRow(title = title, totalCaseText = totalCaseText)
        }
        item {
            StatRingRow(stats)
        }
        item {
            SectionTitle("Response trend for cases")
            TrendTableCard(
                header1 = "In 30 min",
                header2 = "After 30 min",
                header3 = "After 2 hours",
                rows = trendRows,
                colors = Triple(RingBarGreen, TrendBlue, TrendBarRed)
            )
        }
        item {
            SegmentedBarCard(segments)
        }
        item {
            SectionTitle(text = "Cases")
            TwoCardsRow(
                left = { mod ->
                    KeyValueListCard(
                        title ="By services",
                        items = byServices,
                        modifier = mod
                    )
                },
                right = { mod -> KeyValueListCard(title = "By area", items = byArea, modifier = mod) }
            )
        }
    }
}

@Composable
fun StatRingRow(items: List<StatRingUi>) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        items.forEach { ui ->
            StatRingCard(ui, modifier = Modifier.weight(1f))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    FriendshipTheme {
        HomeScreenContent(
            title = "Sun Apr 26 14:00:33 BDT 2026",
            totalCaseText = "Total Case: 1",
            stats = listOf(
                StatRingUi(label = "Pending", value = 1, color = RingBarRed, max = 1),
                StatRingUi(label = "Answered", value = 0, color = RingBarGreen, max = 1),
                StatRingUi(label = "Referred", value = 0, color = RingBarBlue, max = 1)
            ),
            trendRows = listOf(
                TrendRowUi("Total", in30Min = 0, after30Min = 0, after2Hours = 0)
            ),
            segments = listOf(
                SegmentUi(label = "in30", percent = 0f, color = RingBarGreen),
                SegmentUi(label = "after30", percent = 0f, color = TrendBlue),
                SegmentUi(label = "after2h", percent = 0f, color = TrendRed)
            ),
            byServices = listOf(
                KeyValueUi("Loose Stool", 1)
            ),
            byArea = listOf(
                KeyValueUi("Fulchari", 1)
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun StatRingRowPreview() {
    FriendshipTheme {
        StatRingRow(
            items = listOf(
                StatRingUi(label = "Pending", value = 45, color = RingBarRed, max = 100),
                StatRingUi(label = "Answered", value = 75, color = RingBarGreen, max = 100),
                StatRingUi(label = "Referred", value = 10, color = RingBarBlue, max = 100)
            )
        )
    }
}
