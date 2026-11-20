package com.example.demov1.data

import com.example.demov1.R
import com.example.demov1.model.Transaction

object DataManager {
    val sampleTransactions = mutableListOf(
        Transaction(
            id = "Ăn trưa",
            amount = -85_000.0,
            type = "Khoản chi",
            date = "19/11/2026",
            imageUrl = ""
        ),
        Transaction(
            id = "Lương tháng",
            amount = 8_500_000.0,
            type = "Khoản thu",
            date = "18/11/2026",
            imageUrl = ""
        ),
        Transaction(
            id = "Cà phê",
            amount = -42_000.0,
            type = "Khoản chi",
            date = "17/11/2026",
            imageUrl = ""
        )
    )
    fun addTransaction(Transaction: Transaction) {
        sampleTransactions.add(Transaction)
    }
    fun removeTransaction(Transaction: Transaction) {
        sampleTransactions.remove(Transaction)
    }
    fun sumIncome(): Double {
        return sampleTransactions.filter { it.type == "Khoản thu" }.sumOf { it.amount }
        }
    fun sumExpense(): Double {
        return sampleTransactions.filter { it.type == "Khoản chi" }.sumOf { it.amount }
    }
    fun totalMoney(): Double {
        return sampleTransactions.sumOf { it.amount }
    }

    }
