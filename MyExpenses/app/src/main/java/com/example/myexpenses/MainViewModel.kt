package com.example.myexpenses

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myexpenses.data.Category
import com.example.myexpenses.data.Expense
import com.example.myexpenses.data.ExpenseCategory
import com.example.myexpenses.data.ExpenseDatabase
import com.example.myexpenses.data.PaymentMode
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val db = ExpenseDatabase.getDatabase(application)
    private val expenseDao = db.expenseDao()

    init {
        // Initialize default categories and payment modes if none exist
        viewModelScope.launch {
            val existingCats = expenseDao.getAllCategories().first()
            if (existingCats.isEmpty()) {
                ExpenseCategory.entries.forEach {
                    expenseDao.insertCategory(Category(it.name))
                }
            }

            val existingModes = expenseDao.getAllPaymentModes().first()
            if (existingModes.isEmpty()) {
                listOf("CASH", "CREDIT CARD", "DEBIT CARD", "BKASH", "ROCKET", "NAGAD").forEach {
                    expenseDao.insertPaymentMode(PaymentMode(it))
                }
            }
        }
    }

    val categories: StateFlow<List<Category>> = expenseDao.getAllCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val paymentModes: StateFlow<List<PaymentMode>> = expenseDao.getAllPaymentModes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val descriptionSuggestions: StateFlow<List<String>> = expenseDao.getDistinctDescriptions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addCategory(name: String) {
        viewModelScope.launch {
            expenseDao.insertCategory(Category(name.uppercase()))
        }
    }

    fun addPaymentMode(name: String) {
        viewModelScope.launch {
            expenseDao.insertPaymentMode(PaymentMode(name.uppercase()))
        }
    }

    data class MonthYear(val month: Int, val year: Int) {
        fun getName(): String {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.YEAR, year)
            return SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(calendar.time)
        }

        fun getRange(): Pair<Long, Long> {
            val calendar = Calendar.getInstance()
            calendar.set(year, month, 1, 0, 0, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            val start = calendar.timeInMillis
            
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
            calendar.set(Calendar.HOUR_OF_DAY, 23)
            calendar.set(Calendar.MINUTE, 59)
            calendar.set(Calendar.SECOND, 59)
            calendar.set(Calendar.MILLISECOND, 999)
            val end = calendar.timeInMillis
            return start to end
        }
    }

    val availableMonths: List<MonthYear> = List(3) { i ->
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, -i)
        MonthYear(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR))
    }

    val selectedMonth = MutableStateFlow(availableMonths[0])

    @OptIn(ExperimentalCoroutinesApi::class)
    val expenses: StateFlow<List<Expense>> = selectedMonth.flatMapLatest { month ->
        val (start, end) = month.getRange()
        expenseDao.getExpensesForPeriod(start, end)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val totalSpent: StateFlow<Double> = selectedMonth.flatMapLatest { month ->
        val (start, end) = month.getRange()
        expenseDao.getTotalSpentForPeriod(start, end)
    }.map { it ?: 0.0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    // Bottom Sheet State
    var showBottomSheet by mutableStateOf(false)
    var editingExpense by mutableStateOf<Expense?>(null)

    fun openAddExpense() {
        editingExpense = null
        showBottomSheet = true
    }

    fun openEditExpense(expense: Expense) {
        editingExpense = expense
        showBottomSheet = true
    }

    fun closeBottomSheet() {
        showBottomSheet = false
        editingExpense = null
    }

    val categoryTotals: StateFlow<Map<String, Double>> = expenses.map { list ->
        list.groupBy { it.category }.mapValues { entry -> entry.value.sumOf { it.amount } }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    val paymentModeTotals: StateFlow<Map<String, Double>> = expenses.map { list ->
        list.groupBy { it.paymentMode }.mapValues { entry -> entry.value.sumOf { it.amount } }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    val lastThreeMonthsHistory: StateFlow<List<Pair<String, Double>>> = expenseDao.getAllExpenses().map { list ->
        availableMonths.reversed().map { month ->
            val (start, end) = month.getRange()
            val total = list.filter { it.date in start..end }.sumOf { it.amount }
            month.getName().split(" ")[0] to total
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectMonth(month: MonthYear) {
        selectedMonth.value = month
    }

    fun addExpense(expense: Expense) {
        viewModelScope.launch {
            expenseDao.insertExpense(expense)
        }
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            expenseDao.deleteExpense(expense)
        }
    }

    fun exportToCsv(onSuccess: (File) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val currentExpenses = expenses.value
                if (currentExpenses.isEmpty()) {
                    onError("No expenses to export")
                    return@launch
                }

                val fileName = "Expenses_${selectedMonth.value.getName().replace(" ", "_")}.csv"
                val file = File(getApplication<Application>().cacheDir, fileName)
                val writer = FileWriter(file)
                
                // Write CSV Header
                writer.append("ID,Date,Amount,Category,Payment Mode,Description\n")
                
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                
                currentExpenses.forEach { expense ->
                    writer.append("${expense.id},")
                    writer.append("${dateFormat.format(Date(expense.date))},")
                    writer.append("${expense.amount},")
                    writer.append("${expense.category},")
                    writer.append("${expense.paymentMode},")
                    writer.append("\"${expense.description.replace("\"", "\"\"")}\"\n")
                }
                
                writer.flush()
                writer.close()
                onSuccess(file)
            } catch (e: Exception) {
                onError(e.message ?: "Failed to export")
            }
        }
    }
}
