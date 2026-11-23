package com.example.mlk_pocari

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mlk_pocari.data.api.SupabaseClient
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginActivity : AppCompatActivity() {

    private lateinit var prefManager: PrefManager
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvGoToRegister: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        prefManager = PrefManager(this)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbarLogin)
        setSupportActionBar(toolbar)

        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvGoToRegister = findViewById(R.id.tvGoToRegister)

        tvGoToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            // Step 1: Input Validation
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, getString(R.string.login_empty), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            performLogin(username, password)
        }
    }

    private fun performLogin(username: String, password: String) {
        btnLogin.isEnabled = false

        lifecycleScope.launch {
            try {
                // Call Supabase API loginUser (Querying users table)
                val users = SupabaseClient.instance.loginUser("eq.$username", "eq.$password")

                if (users.isNotEmpty()) {
                    val user = users.first()
                    prefManager.isLoggedIn = true
                    prefManager.username = user.username
                    prefManager.userId = user.id ?: 1L

                    Toast.makeText(this@LoginActivity, "Login Berhasil! Selamat datang, ${user.username}", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this@LoginActivity, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)
                    finish()
                } else if ((username == "admin") && (password == "admin123")) {
                    // Demo fallback
                    prefManager.isLoggedIn = true
                    prefManager.username = "admin"
                    prefManager.userId = 1L

                    Toast.makeText(this@LoginActivity, "Login Admin Berhasil!", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this@LoginActivity, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@LoginActivity, getString(R.string.login_failed), Toast.LENGTH_SHORT).show()
                }
            } catch (e: HttpException) {
                if ((username == "admin") && (password == "admin123")) {
                    prefManager.isLoggedIn = true
                    prefManager.username = "admin"
                    prefManager.userId = 1L

                    Toast.makeText(this@LoginActivity, "Login Admin Berhasil!", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this@LoginActivity, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@LoginActivity, "Login gagal (${e.code()}): Username/Password salah!", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                if ((username == "admin") && (password == "admin123")) {
                    prefManager.isLoggedIn = true
                    prefManager.username = "admin"
                    prefManager.userId = 1L

                    Toast.makeText(this@LoginActivity, "Login Admin Berhasil!", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this@LoginActivity, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@LoginActivity, "Gagal terhubung ke server: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            } finally {
                btnLogin.isEnabled = true
            }
        }
    }
}