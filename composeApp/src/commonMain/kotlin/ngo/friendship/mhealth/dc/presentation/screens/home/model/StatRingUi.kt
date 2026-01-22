package ngo.friendship.mhealth.dc.presentation.screens.home.model

import androidx.compose.ui.graphics.Color

data class StatRingUi(
    val label: String,
    val value: Int,
    val color: Color,
    val max: Int = 100
)
data class TrendRowUi(
    val name: String,
    val in30Min: Int,
    val after30Min: Int,
    val after2Hours: Int
)

data class SegmentUi(
    val label: String,
    val percent: Float,
    val color: Color
)

data class KeyValueUi(
    val key: String,
    val value: Int
)