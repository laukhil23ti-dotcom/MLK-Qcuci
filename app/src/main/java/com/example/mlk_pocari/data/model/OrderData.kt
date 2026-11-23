package com.example.mlk_pocari.data.model

import com.google.gson.annotations.SerializedName

data class OrderData(
    @SerializedName("id")
    val id: Long? = null,
    @SerializedName("user_id")
    val userId: Long,
    @SerializedName("service_name")
    val serviceName: String,
    @SerializedName("weight_qty")
    val weightQty: String,
    @SerializedName("total_price")
    val totalPrice: Long,
    @SerializedName("status")
    val status: String,
    @SerializedName("created_at")
    val createdAt: String? = null,
)