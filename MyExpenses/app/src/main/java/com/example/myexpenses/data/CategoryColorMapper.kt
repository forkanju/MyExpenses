package com.example.myexpenses.data

import androidx.compose.ui.graphics.Color
import com.example.myexpenses.ui.theme.CategoryFood
import com.example.myexpenses.ui.theme.CategoryOthers
import com.example.myexpenses.ui.theme.CategoryShopping
import com.example.myexpenses.ui.theme.CategoryTransport

fun String.toCategoryColor(): Color {
    return try {
        when (ExpenseCategory.valueOf(this.uppercase())) {
            ExpenseCategory.FOOD -> CategoryFood
            ExpenseCategory.TRANSPORT -> CategoryTransport
            ExpenseCategory.SHOPPING -> CategoryShopping
            ExpenseCategory.OTHERS -> CategoryOthers
        }
    } catch (e: IllegalArgumentException) {
        CategoryOthers // Default color for custom categories
    }
}
