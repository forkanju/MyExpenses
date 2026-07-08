package com.example.myexpenses.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "payment_modes")
data class PaymentMode(
    @PrimaryKey
    val name: String
)
