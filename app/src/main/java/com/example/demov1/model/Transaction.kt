package com.example.demov1.model

data class Transaction(
    val id: String,
    val title: String,
    val amount: Double,
    val type: String,
    val date: String,
    val imageUrl: String
) {

}