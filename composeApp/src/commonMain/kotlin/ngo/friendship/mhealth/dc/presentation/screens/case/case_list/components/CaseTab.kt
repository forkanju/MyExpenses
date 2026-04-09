package ngo.friendship.mhealth.dc.presentation.screens.case.case_list.components

import androidx.compose.ui.graphics.Color

enum class CaseTab(val apiParam: String, val label: String, val color: Color) {
    Pending("New", "Pending", Color(0xFFFF6B6B)),
    Opened("Open", "Opened", Color(0xFF2D7FF9)),
    Older("Older", "Older", Color(0xFFFF9800)),
    Answered("Close", "Answered", Color(0xFF4CAF50)),
}