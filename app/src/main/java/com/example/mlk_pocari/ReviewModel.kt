package com.example.mlk_pocari

data class ReviewModel(
    val id: String,
    val username: String,
    val rating: Float,
    val comment: String,
    val date: String,
)