package ngo.friendship.mhealth.dc.presentation.screens.main.case_list.components

import androidx.compose.ui.graphics.Color

enum class CaseTab(val label: String, val color: Color) {
    Pending("Pending", Color(0xFFFF6B6B)),
    Opened("Opened", Color(0xFF2D7FF9)),
    Timeover("Timeover", Color(0xFF777777)),
    Older("Older", Color(0xFF777777)),
    Answered("Answered", Color(0xFF777777)),
}