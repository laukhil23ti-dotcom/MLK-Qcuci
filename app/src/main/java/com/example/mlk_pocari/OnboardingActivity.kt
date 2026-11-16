package com.example.mlk_pocari

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2

class OnboardingActivity : AppCompatActivity() {

    private lateinit var prefManager: PrefManager
    private lateinit var viewPager: ViewPager2
    private lateinit var btnSkip: Button
    private lateinit var btnNext: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        prefManager = PrefManager(this)

        viewPager = findViewById(R.id.viewPagerOnboarding)
        btnSkip = findViewById(R.id.btnSkip)
        btnNext = findViewById(R.id.btnNext)

        val onboardingList = listOf(
            OnboardingModel(
                title = getString(R.string.onboarding_title_1),
                description = getString(R.string.onboarding_desc_1),
                imageResId = R.drawable.qcuci,
            ),
            OnboardingModel(
                title = getString(R.string.onboarding_title_2),
                description = getString(R.string.onboarding_desc_2),
                imageResId = R.drawable.ic_nav_services,
            ),
            OnboardingModel(
                title = getString(R.string.onboarding_title_3),
                description = getString(R.string.onboarding_desc_3),
                imageResId = R.drawable.ic_nav_features,
            ),
        )

        val adapter = OnboardingAdapter(onboardingList)
        viewPager.adapter = adapter

        viewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    if (position == (onboardingList.size - 1)) {
                        btnNext.text = getString(R.string.btn_get_started)
                    } else {
                        btnNext.text = getString(R.string.btn_next)
                    }
                }
            },
        )

        btnSkip.setOnClickListener {
            finishOnboarding()
        }

        btnNext.setOnClickListener {
            if (viewPager.currentItem < (onboardingList.size - 1)) {
                viewPager.currentItem += 1
            } else {
                finishOnboarding()
            }
        }
    }

    private fun finishOnboarding() {
        prefManager.isOnboardingCompleted = true
        val targetActivity = if (prefManager.isLoggedIn) {
            MainActivity::class.java
        } else {
            LoginActivity::class.java
        }
        val intent = Intent(this, targetActivity).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }
}