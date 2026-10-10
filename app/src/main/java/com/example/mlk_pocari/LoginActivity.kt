package com.example.mlk_pocari

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar

class LoginActivity : AppCompatActivity() {

    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        prefManager = PrefManager(this)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbarLogin)
        setSupportActionBar(toolbar)

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            // Step 1: Input Validation
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, getString(R.string.login_empty), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Step 2: Credential Check (admin/admin123 or valid credentials)
            val isValidCredential = ((username == "admin") && (password == "admin123")) ||
                    (username.isNotEmpty() && (password.length >= 4))

            if (isValidCredential) {
                // Step 3: Save Session to SharedPreferences
                prefManager.isLoggedIn = true
                prefManager.username = username

                Toast.makeText(this, "Login Berhasil! Selamat datang, $username", Toast.LENGTH_SHORT).show()

                // Step 4: Navigate to MainActivity & Clear Backstack
                val intent = Intent(this, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, getString(R.string.login_failed), Toast.LENGTH_SHORT).show()
            }
        }
    }
}