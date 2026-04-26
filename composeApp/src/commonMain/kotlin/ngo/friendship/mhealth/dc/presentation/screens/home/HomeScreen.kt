package ngo.friendship.mhealth.dc.presentation.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ngo.friendship.mhealth.dc.presentation.screens.home.components.HomeTopRow
import ngo.friendship.mhealth.dc.presentation.screens.home.components.KeyValueListCard
import ngo.friendship.mhealth.dc.presentation.screens.home.components.SectionTitle
import ngo.friendship.mhealth.dc.presentation.screens.home.components.SegmentedBarCard
import ngo.friendship.mhealth.dc.presentation.screens.home.components.StatRingCard
import ngo.friendship.mhealth.dc.presentation.screens.home.components.TrendTableCard
import ngo.friendship.mhealth.dc.presentation.screens.home.components.TwoCardsRow
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
    val dashboardData by viewModel.dashboardState

    val statusSummary = dashboardData?.data?.statusSummary
    val timeSummary = dashboardData?.data?.timeSummary
    val total = statusSummary?.total ?: 0

    val stats = listOf(
        StatRingUi("Pending", statusSummary?.pending ?: 0, RingBarRed, max = total.coerceAtLeast(1)),
        StatRingUi("Answered", statusSummary?.answered ?: 0, RingBarGreen, max = total.coerceAtLeast(1)),
        StatRingUi("Referred", statusSummary?.referred ?: 0, RingBarBlue, max = total.coerceAtLeast(1))
    )

    val trendRows = listOf(
        TrendRowUi("Total", timeSummary?.in30Min ?: 0, timeSummary?.after30Min ?: 0, timeSummary?.after2Hours ?: 0)
    )

    val totalTime = ((timeSummary?.in30Min ?: 0) + (timeSummary?.after30Min ?: 0) + (timeSummary?.after2Hours ?: 0)).coerceAtLeast(1).toFloat()
    val segments = listOf(
        SegmentUi("in30", (timeSummary?.in30Min ?: 0) / totalTime * 100f, RingBarGreen),
        SegmentUi("after30", (timeSummary?.after30Min ?: 0) / totalTime * 100f, TrendBlue),
        SegmentUi("after2h", (timeSummary?.after2Hours ?: 0) / totalTime * 100f, TrendRed)
    )

    val byServices = dashboardData?.data?.topQuestionnaires?.map {
        KeyValueUi(it.questionnaireTitle ?: "", it.totalCount ?: 0)
    } ?: emptyList()

    val byArea = dashboardData?.data?.topUpazila?.map {
        KeyValueUi(it.upazilaName ?: "", it.totalService ?: 0)
    } ?: emptyList()

    val responseTimeText = dashboardData?.responseTime ?: "Today"

    HomeScreenContent(
        modifier = modifier,
        title = "Today",
        totalCaseText = "Total Case: $total",
        stats = stats,
        trendRows = trendRows,
        segments = segments,
        byServices = byServices,
        byArea = byArea
    )
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
        contentPadding = PaddingValues(12.dp),
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
            SectionTitle("Cases")
            TwoCardsRow(
                left = { mod -> KeyValueListCard("By services", byServices, modifier = mod) },
                right = { mod -> KeyValueListCard("By area", byArea, modifier = mod) }
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
                StatRingUi("Pending", 1, RingBarRed, max = 1),
                StatRingUi("Answered", 0, RingBarGreen, max = 1),
                StatRingUi("Referred", 0, RingBarBlue, max = 1)
            ),
            trendRows = listOf(
                TrendRowUi("Total", 0, 0, 0)
            ),
            segments = listOf(
                SegmentUi("in30", 0f, RingBarGreen),
                SegmentUi("after30", 0f, TrendBlue),
                SegmentUi("after2h", 0f, TrendRed)
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
                StatRingUi("Pending", 45, RingBarRed, max = 100),
                StatRingUi("Answered", 75, RingBarGreen, max = 100),
                StatRingUi("Referred", 10, RingBarBlue, max = 100)
            )
        )
    }
}
