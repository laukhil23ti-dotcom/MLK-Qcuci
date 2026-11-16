package com.example.mlk_pocari

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        prefManager = PrefManager(this)

        Handler(Looper.getMainLooper()).postDelayed(
            {
                val targetActivity = when {
                    !prefManager.isOnboardingCompleted -> OnboardingActivity::class.java
                    prefManager.isLoggedIn -> MainActivity::class.java
                    else -> LoginActivity::class.java
                }
                val intent = Intent(this, targetActivity)
                startActivity(intent)
                finish()
            },
            SPLASH_DELAY_MS,
        )
    }

    companion object {
        private const val SPLASH_DELAY_MS = 2000L
    }
}