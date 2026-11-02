package com.example.mlk_pocari

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import org.json.JSONArray
import org.json.JSONObject

class ReviewManager(context: Context) {

    private val pref: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun getReviews(): List<ReviewModel> {
        val jsonString = pref.getString(KEY_REVIEWS, null)
        if (jsonString.isNullOrEmpty()) {
            val defaultList = getDefaultReviews()
            saveReviewsToPref(defaultList)
            return defaultList
        }

        val list = mutableListOf<ReviewModel>()
        try {
            val jsonArray = JSONArray(jsonString)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                list.add(
                    ReviewModel(
                        id = obj.optString("id", ""),
                        username = obj.optString("username", "Pelanggan Qcuci"),
                        rating = obj.optDouble("rating", 5.0).toFloat(),
                        comment = obj.optString("comment", ""),
                        date = obj.optString("date", ""),
                    ),
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    fun addReview(review: ReviewModel) {
        val currentList = getReviews().toMutableList()
        currentList.add(0, review)
        saveReviewsToPref(currentList)
    }

    private fun saveReviewsToPref(list: List<ReviewModel>) {
        val jsonArray = JSONArray()
        list.forEach { item ->
            val obj = JSONObject().apply {
                put("id", item.id)
                put("username", item.username)
                put("rating", item.rating.toDouble())
                put("comment", item.comment)
                put("date", item.date)
            }
            jsonArray.put(obj)
        }
        pref.edit { putString(KEY_REVIEWS, jsonArray.toString()) }
    }

    private fun getDefaultReviews(): List<ReviewModel> {
        return listOf(
            ReviewModel(
                id = "REV-101",
                username = "Ahmad Pratama",
                rating = 5.0f,
                comment = "Layanan sangat cepat, baju wangi dan rapi banget!",
                date = "01 Nov 2026",
            ),
            ReviewModel(
                id = "REV-102",
                username = "Siti Rahma",
                rating = 4.5f,
                comment = "Cuci kiloan express tepat waktu, pelayanan staf sangat ramah.",
                date = "28 Oct 2026",
            ),
        )
    }

    companion object {
        private const val PREF_NAME = "QcuciReviewPref"
        private const val KEY_REVIEWS = "reviews_list"
    }
}