package com.example.myexpenses.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAllExpenses(): Flow<List<Expense>>

    @Query("SELECT * FROM expenses WHERE date >= :startDate AND date <= :endDate ORDER BY date DESC")
    fun getExpensesForPeriod(startDate: Long, endDate: Long): Flow<List<Expense>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: Expense)

    @Delete
    suspend fun deleteExpense(expense: Expense)

    @Query("SELECT SUM(amount) FROM expenses")
    fun getTotalSpent(): Flow<Double?>

    @Query("SELECT SUM(amount) FROM expenses WHERE date >= :startDate AND date <= :endDate")
    fun getTotalSpentForPeriod(startDate: Long, endDate: Long): Flow<Double?>

    @Query("SELECT * FROM categories ORDER BY name ASC")
    fun getAllCategories(): Flow<List<Category>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategory(category: Category)

    @Query("SELECT * FROM payment_modes ORDER BY name ASC")
    fun getAllPaymentModes(): Flow<List<PaymentMode>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPaymentMode(paymentMode: PaymentMode)

    @Query("SELECT DISTINCT description FROM expenses ORDER BY description ASC")
    fun getDistinctDescriptions(): Flow<List<String>>
}
