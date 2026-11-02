package com.example.mlk_pocari

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RatingActivity : AppCompatActivity() {

    private lateinit var reviewManager: ReviewManager
    private lateinit var prefManager: PrefManager

    private lateinit var ratingBar: RatingBar
    private lateinit var etComment: EditText
    private lateinit var btnSubmitReview: Button
    private lateinit var containerReviews: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating)

        reviewManager = ReviewManager(this)
        prefManager = PrefManager(this)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbarRating)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        toolbar.setNavigationOnClickListener {
            finish()
        }

        ratingBar = findViewById(R.id.ratingBar)
        etComment = findViewById(R.id.etComment)
        btnSubmitReview = findViewById(R.id.btnSubmitReview)
        containerReviews = findViewById(R.id.containerReviews)

        displayReviews()

        btnSubmitReview.setOnClickListener {
            val rating = ratingBar.rating
            val comment = etComment.text.toString().trim()

            // Step 1: Validation (Requirement 8: Rating must be > 0)
            if (rating <= 0f) {
                Toast.makeText(this, getString(R.string.rating_validation_empty), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Step 2: Prepare Review Data
            val localeID = Locale.Builder().setLanguage("id").setRegion("ID").build()
            val dateFormat = SimpleDateFormat("dd MMM yyyy", localeID)
            val currentDate = dateFormat.format(Date())

            val currentUsername = prefManager.username.ifEmpty { "Pelanggan Qcuci" }
            val finalComment = comment.ifEmpty { "Layanan laundry sangat baik, bersih, dan wangi." }

            val newReview = ReviewModel(
                id = "REV-${System.currentTimeMillis()}",
                username = currentUsername,
                rating = rating,
                comment = finalComment,
                date = currentDate,
            )

            // Step 3: Save Review via ReviewManager
            reviewManager.addReview(newReview)

            // Step 4: Show Success Feedback (Requirement 5)
            Toast.makeText(this, getString(R.string.rating_success), Toast.LENGTH_SHORT).show()

            // Step 5: Reset Form & Refresh History List
            ratingBar.rating = 0f
            etComment.setText("")
            displayReviews()
        }
    }

    private fun displayReviews() {
        containerReviews.removeAllViews()
        val reviews = reviewManager.getReviews()
        val inflater = LayoutInflater.from(this)

        reviews.forEach { item ->
            val itemView = inflater.inflate(R.layout.item_review, containerReviews, false)

            val tvUser = itemView.findViewById<TextView>(R.id.tvReviewUser)
            val tvStars = itemView.findViewById<TextView>(R.id.tvReviewStars)
            val tvDate = itemView.findViewById<TextView>(R.id.tvReviewDate)
            val tvComment = itemView.findViewById<TextView>(R.id.tvReviewComment)

            tvUser.text = item.username
            tvStars.text = getString(R.string.star_rating_format, item.rating)
            tvDate.text = item.date
            tvComment.text = item.comment

            containerReviews.addView(itemView)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}