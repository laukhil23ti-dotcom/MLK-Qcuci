package com.example.mlk_pocari.data.api

import com.example.mlk_pocari.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SupabaseClient {

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val originalRequest = chain.request()
            val newRequest = originalRequest.newBuilder()
                .header("apikey", BuildConfig.SUPABASE_KEY)
                .header("Authorization", "Bearer ${BuildConfig.SUPABASE_KEY}")
                .header("Content-Type", "application/json")
                .build()
            chain.proceed(newRequest)
        }
        .build()

    val instance: SupabaseApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.SUPABASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SupabaseApiService::class.java)
    }
}