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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mlk_pocari.data.api.SupabaseClient
import com.example.mlk_pocari.data.model.ReviewData
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RatingActivity : AppCompatActivity() {

    private lateinit var prefManager: PrefManager

    private lateinit var ratingBar: RatingBar
    private lateinit var etComment: EditText
    private lateinit var btnSubmitReview: Button
    private lateinit var containerReviews: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating)

        prefManager = PrefManager(this)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbarRating)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        toolbar.setNavigationOnClickListener { finish() }

        ratingBar = findViewById(R.id.ratingBar)
        etComment = findViewById(R.id.etComment)
        btnSubmitReview = findViewById(R.id.btnSubmitReview)
        containerReviews = findViewById(R.id.containerReviews)

        displayReviews()

        btnSubmitReview.setOnClickListener {
            val rating = ratingBar.rating
            val comment = etComment.text.toString().trim()

            // Step 1: Validation (Rating must be > 0)
            if (rating <= 0f) {
                Toast.makeText(this, getString(R.string.rating_validation_empty), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val currentUsername = prefManager.username.ifEmpty { "Pelanggan Qcuci" }
            val currentUserId = if (prefManager.userId > 0) prefManager.userId else 1L
            val finalComment = comment.ifEmpty { "Layanan laundry sangat baik, bersih, dan wangi." }

            val newReview = ReviewData(
                userId = currentUserId,
                username = currentUsername,
                rating = rating,
                comment = finalComment,
            )

            submitReview(newReview)
        }
    }

    private fun submitReview(review: ReviewData) {
        btnSubmitReview.isEnabled = false

        lifecycleScope.launch {
            try {
                SupabaseClient.instance.addReview(review)
                Toast.makeText(this@RatingActivity, getString(R.string.rating_success), Toast.LENGTH_SHORT).show()
                ratingBar.rating = 0f
                etComment.setText("")
                displayReviews()
            } catch (e: Exception) {
                Toast.makeText(this@RatingActivity, "Gagal mengirim ulasan: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            } finally {
                btnSubmitReview.isEnabled = true
            }
        }
    }

    private fun displayReviews() {
        containerReviews.removeAllViews()

        lifecycleScope.launch {
            try {
                val reviews = SupabaseClient.instance.getReviews()
                val inflater = LayoutInflater.from(this@RatingActivity)

                reviews.forEach { item ->
                    val itemView = inflater.inflate(R.layout.item_review, containerReviews, false)

                    val tvUser = itemView.findViewById<TextView>(R.id.tvReviewUser)
                    val tvStars = itemView.findViewById<TextView>(R.id.tvReviewStars)
                    val tvDate = itemView.findViewById<TextView>(R.id.tvReviewDate)
                    val tvComment = itemView.findViewById<TextView>(R.id.tvReviewComment)

                    val isMyUserById = (prefManager.userId > 0L) && (item.userId == prefManager.userId)
                    val isMyUserByName = prefManager.username.isNotEmpty() && item.username.equals(prefManager.username, ignoreCase = true)
                    val isMyReview = isMyUserById || isMyUserByName

                    tvUser.text = if (isMyReview) "${item.username} (Saya)" else item.username
                    tvStars.text = getString(R.string.star_rating_format, item.rating)
                    tvDate.text = item.createdAt?.take(10) ?: "Hari ini"
                    tvComment.text = item.comment

                    // Allow Edit/Delete if review belongs to logged-in user
                    if (isMyReview) {
                        itemView.setOnClickListener {
                            showReviewOptionsDialog(item)
                        }
                    }

                    containerReviews.addView(itemView)
                }
            } catch (e: HttpException) {
                Toast.makeText(this@RatingActivity, "Error (${e.code()}): ${e.message()}", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this@RatingActivity, "Gagal memuat ulasan: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showReviewOptionsDialog(review: ReviewData) {
        val options = arrayOf("Edit Ulasan", "Hapus Ulasan")
        AlertDialog.Builder(this)
            .setTitle("Kelola Ulasan Anda")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> showEditReviewDialog(review)
                    1 -> showDeleteReviewDialog(review)
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun showEditReviewDialog(review: ReviewData) {
        val etCommentField = EditText(this).apply {
            setText(review.comment)
            setPadding(32, 32, 32, 32)
        }

        AlertDialog.Builder(this)
            .setTitle("Edit Komentar Ulasan")
            .setView(etCommentField)
            .setPositiveButton("Simpan") { _, _ ->
                val updatedComment = etCommentField.text.toString().trim()
                if (updatedComment.isNotEmpty()) {
                    val updatedReview = ReviewData(
                        userId = review.userId,
                        username = review.username,
                        rating = review.rating,
                        comment = updatedComment,
                    )
                    updateReview(review.id ?: 0L, updatedReview)
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun updateReview(id: Long, review: ReviewData) {
        lifecycleScope.launch {
            try {
                SupabaseClient.instance.updateReview("eq.$id", review)
                Toast.makeText(this@RatingActivity, "Ulasan berhasil diperbarui!", Toast.LENGTH_SHORT).show()
                displayReviews()
            } catch (e: Exception) {
                Toast.makeText(this@RatingActivity, "Gagal memperbarui ulasan: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showDeleteReviewDialog(review: ReviewData) {
        AlertDialog.Builder(this)
            .setTitle("Hapus Ulasan")
            .setMessage("Apakah Anda yakin ingin menghapus ulasan ini?")
            .setPositiveButton("Hapus") { _, _ ->
                deleteReview(review.id ?: 0L)
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun deleteReview(id: Long) {
        lifecycleScope.launch {
            try {
                SupabaseClient.instance.deleteReview("eq.$id")
                Toast.makeText(this@RatingActivity, "Ulasan berhasil dihapus!", Toast.LENGTH_SHORT).show()
                displayReviews()
            } catch (e: Exception) {
                Toast.makeText(this@RatingActivity, "Gagal menghapus ulasan: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            }
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