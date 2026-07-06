package com.example.myexpenses.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

object CurrencyUtils {
    fun formatBDT(amount: Double): String {
        val symbols = DecimalFormatSymbols(Locale.US).apply {
            currencySymbol = "৳"
        }
        val formatter = DecimalFormat("¤#,##,##0.00", symbols)
        return formatter.format(amount)
    }
}
