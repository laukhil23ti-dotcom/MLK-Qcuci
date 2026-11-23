package com.example.mlk_pocari.data.api

import com.example.mlk_pocari.data.model.OrderData
import com.example.mlk_pocari.data.model.ReviewData
import com.example.mlk_pocari.data.model.UserModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface SupabaseApiService {

    // --- USERS ---
    @Headers(
        "Content-Profile: mobile",
        "Prefer: return=representation",
    )
    @POST("users")
    suspend fun registerUser(@Body user: UserModel): List<UserModel>

    @Headers("Accept-Profile: mobile")
    @GET("users")
    suspend fun loginUser(
        @Query("username") usernameFilter: String,
        @Query("password") passwordFilter: String,
    ): List<UserModel>

    @Headers("Accept-Profile: mobile")
    @GET("users")
    suspend fun checkUsername(
        @Query("username") usernameFilter: String,
    ): List<UserModel>

    // --- ORDERS ---
    @Headers("Accept-Profile: mobile")
    @GET("orders")
    suspend fun getOrders(
        @Query("order") order: String = "created_at.desc",
    ): List<OrderData>

    @Headers("Accept-Profile: mobile")
    @GET("orders")
    suspend fun getOrdersByUser(
        @Query("user_id") userIdFilter: String,
        @Query("order") order: String = "created_at.desc",
    ): List<OrderData>

    @Headers(
        "Content-Profile: mobile",
        "Prefer: return=representation",
    )
    @POST("orders")
    suspend fun addOrder(@Body order: OrderData): List<OrderData>

    @Headers(
        "Content-Profile: mobile",
        "Prefer: return=representation",
    )
    @PATCH("orders")
    suspend fun updateOrder(
        @Query("id") idFilter: String,
        @Body orderData: OrderData,
    ): List<OrderData>

    @Headers("Content-Profile: mobile")
    @DELETE("orders")
    suspend fun deleteOrder(
        @Query("id") idFilter: String,
    ): Response<Unit>

    // --- REVIEWS ---
    @Headers("Accept-Profile: mobile")
    @GET("reviews")
    suspend fun getReviews(
        @Query("order") order: String = "created_at.desc",
    ): List<ReviewData>

    @Headers(
        "Content-Profile: mobile",
        "Prefer: return=representation",
    )
    @POST("reviews")
    suspend fun addReview(@Body review: ReviewData): List<ReviewData>

    @Headers(
        "Content-Profile: mobile",
        "Prefer: return=representation",
    )
    @PATCH("reviews")
    suspend fun updateReview(
        @Query("id") idFilter: String,
        @Body reviewData: ReviewData,
    ): List<ReviewData>

    @Headers("Content-Profile: mobile")
    @DELETE("reviews")
    suspend fun deleteReview(
        @Query("id") idFilter: String,
    ): Response<Unit>
}