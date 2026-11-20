package com.example.demov1.data

import com.example.demov1.R
import com.example.demov1.model.Transaction

object DataManager {
    val sampleTransactions = mutableListOf(
        Transaction(
            id = "2",
            title = "Ăn sáng",
            amount = -5.000,
            type = "Khoản chi",
            date = "21/11/2026",
            imageUrl = ""
        ),
        Transaction(
            id = "1",
            title = "Mỳ tôm",
            amount = -10.000,
            type = "Khoản chi",
            date = "21/11/2026",
            imageUrl = ""

        ),
        Transaction(
            id = "1",
            title = "Tiền lương",
            amount = 30.000,
            type = "Khoản chi",
            date = "21/11/2026",
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
