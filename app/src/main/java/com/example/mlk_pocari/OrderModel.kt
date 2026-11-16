package com.example.mlk_pocari

data class OrderModel(
    val orderId: String,
    val serviceName: String,
    val weightOrQty: String,
    val totalPrice: String,
    val date: String,
    val status: String,
)