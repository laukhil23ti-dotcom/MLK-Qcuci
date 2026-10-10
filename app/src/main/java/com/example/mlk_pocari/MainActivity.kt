package com.example.mlk_pocari

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar

class MainActivity : AppCompatActivity() {

    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prefManager = PrefManager(this)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbarMain)
        setSupportActionBar(toolbar)

        val btnMyProject = findViewById<Button>(R.id.btnMyProject)
        val btnCalculator = findViewById<Button>(R.id.btnCalculator)
        val btnWebsite = findViewById<Button>(R.id.btnWebsite)
        val btnDeveloper = findViewById<Button>(R.id.btnDeveloper)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        btnMyProject.setOnClickListener {
            val intent = Intent(this, DetailActivity::class.java)
            startActivity(intent)
        }

        btnCalculator.setOnClickListener {
            val intent = Intent(this, CalculatorActivity::class.java)
            startActivity(intent)
        }

        btnWebsite.setOnClickListener {
            val intent = Intent(this, WebActivity::class.java)
            startActivity(intent)
        }

        btnDeveloper.setOnClickListener {
            val intent = Intent(this, DeveloperActivity::class.java)
            startActivity(intent)
        }

        btnLogout.setOnClickListener {
            prefManager.logout()
            Toast.makeText(this, "Berhasil logout", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            finish()
        }
    }
}