package com.example.mlk_pocari

import android.os.Bundle
import android.util.Patterns
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mlk_pocari.data.api.SupabaseClient
import com.example.mlk_pocari.data.model.UserModel
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegisterActivity : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var tvGoToLogin: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbarRegister)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        toolbar.setNavigationOnClickListener { finish() }

        etUsername = findViewById(R.id.etRegisterUsername)
        etEmail = findViewById(R.id.etRegisterEmail)
        etPassword = findViewById(R.id.etRegisterPassword)
        etConfirmPassword = findViewById(R.id.etRegisterConfirmPassword)
        btnRegister = findViewById(R.id.btnRegister)
        tvGoToLogin = findViewById(R.id.tvGoToLogin)

        tvGoToLogin.setOnClickListener { finish() }

        btnRegister.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()

            // 1. Validasi Input
            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, getString(R.string.register_empty), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, getString(R.string.register_invalid_email), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(this, getString(R.string.register_short_password), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, getString(R.string.register_password_mismatch), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            performRegister(username, email, password)
        }
    }

    private fun performRegister(username: String, email: String, password: String) {
        btnRegister.isEnabled = false

        lifecycleScope.launch {
            try {
                // Check if username already exists
                val existingUsers = SupabaseClient.instance.checkUsername("eq.$username")
                if (existingUsers.isNotEmpty()) {
                    Toast.makeText(this@RegisterActivity, getString(R.string.register_username_exists), Toast.LENGTH_LONG).show()
                    btnRegister.isEnabled = true
                    return@launch
                }

                val newUser = UserModel(
                    username = username,
                    email = email,
                    password = password,
                )

                val result = SupabaseClient.instance.registerUser(newUser)
                if (result.isNotEmpty()) {
                    Toast.makeText(this@RegisterActivity, getString(R.string.register_success), Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@RegisterActivity, getString(R.string.register_success), Toast.LENGTH_SHORT).show()
                    finish()
                }
            } catch (e: HttpException) {
                if (e.code() == 409) {
                    Toast.makeText(this@RegisterActivity, getString(R.string.register_username_exists), Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@RegisterActivity, "Error (${e.code()}): ${e.message()}", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@RegisterActivity, "Koneksi gagal: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            } finally {
                btnRegister.isEnabled = true
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