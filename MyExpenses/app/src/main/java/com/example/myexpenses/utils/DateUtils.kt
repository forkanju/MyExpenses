package com.example.myexpenses.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateUtils {
    fun formatExpenseDate(timestamp: Long): String {
        val now = Calendar.getInstance()
        val expenseDate = Calendar.getInstance().apply { timeInMillis = timestamp }

        return when {
            isSameDay(now, expenseDate) -> {
                SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(timestamp))
            }
            isYesterday(now, expenseDate) -> {
                "Yesterday"
            }
            else -> {
                SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(timestamp))
            }
        }
    }

    fun isSameDay(cal1: Calendar, cal2: Calendar): Boolean {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }

    private fun isYesterday(now: Calendar, expenseDate: Calendar): Boolean {
        val yesterday = Calendar.getInstance().apply {
            timeInMillis = now.timeInMillis
            add(Calendar.DAY_OF_YEAR, -1)
        }
        return isSameDay(yesterday, expenseDate)
    }
}
