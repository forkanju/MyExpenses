package ngo.friendship.mhealth.dc.presentation.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ngo.friendship.mhealth.dc.presentation.screens.home.components.HomeScreenScaffold
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
import ngo.friendship.mhealth.dc.presentation.theme.RingBarBlue
import ngo.friendship.mhealth.dc.presentation.theme.RingBarGreen
import ngo.friendship.mhealth.dc.presentation.theme.RingBarRed
import ngo.friendship.mhealth.dc.presentation.theme.TrendBarRed
import ngo.friendship.mhealth.dc.presentation.theme.TrendBlue
import ngo.friendship.mhealth.dc.presentation.theme.TrendRed

@Composable
fun HomeScreen(
) {
    val stats = listOf(
        StatRingUi("Pending", 45, RingBarRed, max = 100),
        StatRingUi("Answered", 75, RingBarGreen, max = 100),
        StatRingUi("Referred", 10, RingBarBlue, max = 100)
    )

    val trendRows = listOf(
        TrendRowUi("Myself", 45, 5, 33),
        TrendRowUi("Central", 0, 16, 20)
    )

    val segments = listOf(
        SegmentUi("in30", 35f, RingBarGreen),
        SegmentUi("after30", 25f, TrendBlue),
        SegmentUi("after2h", 10f, TrendRed)
    )

    val byServices = listOf(
        KeyValueUi("Oral ulcer", 22),
        KeyValueUi("Fever", 21),
        KeyValueUi("Pregnancy", 16),
        KeyValueUi("Eye", 15),
        KeyValueUi("Ear Problem", 11),
    )

    val byArea = listOf(
        KeyValueUi("Mongla", 22),
        KeyValueUi("Chilmari", 21),
        KeyValueUi("Bhola Sadar", 16),
        KeyValueUi("Sundorgonj", 15),
        KeyValueUi("Shyamnagar", 11),
    )

    HomeScreenScaffold(
        title = "Today",
        totalCaseText = "Total Case: 198"
    ) {
        StatRingRow(stats)

        SectionTitle("Response trend for cases")
        TrendTableCard(
            header1 = "In 30 min",
            header2 = "After 30 min",
            header3 = "After 2 hours",
            rows = trendRows,
            colors = Triple(RingBarGreen, TrendBlue, TrendBarRed)
        )

        Spacer(Modifier.height(10.dp))
        SegmentedBarCard(segments)

        SectionTitle("Cases")
        TwoCardsRow(
            left = { mod -> KeyValueListCard("By services", byServices, modifier = mod) },
            right = { mod -> KeyValueListCard("By area", byArea, modifier = mod) }
        )
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
