package com.example.mlk_pocari.data.model

import com.google.gson.annotations.SerializedName

data class ReviewData(
    @SerializedName("id")
    val id: Long? = null,
    @SerializedName("user_id")
    val userId: Long,
    @SerializedName("username")
    val username: String,
    @SerializedName("rating")
    val rating: Float,
    @SerializedName("comment")
    val comment: String,
    @SerializedName("created_at")
    val createdAt: String? = null,
)