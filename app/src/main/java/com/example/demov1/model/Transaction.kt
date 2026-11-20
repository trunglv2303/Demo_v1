package com.example.demov1.model

data class Transaction(
    val id: Int,
    val amount: Double,
    val type: String,
    val date: String,
    val imageUrl: String
)