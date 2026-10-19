package com.example.mlk_pocari

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var toolbar: MaterialToolbar
    private lateinit var bottomNavigation: BottomNavigationView

    private val homeFragment = HomeFragment()
    private val servicesFragment = ServicesFragment()
    private val featureContainerFragment = FeatureContainerFragment()
    private val profileFragment = ProfileFragment()

    private var activeFragment: Fragment = homeFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbarMain)
        setSupportActionBar(toolbar)

        bottomNavigation = findViewById(R.id.bottom_navigation)

        // Setup Fragment Container with FragmentManager (Avoid recreating fragments)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().apply {
                add(R.id.fragment_container, profileFragment, "4").hide(profileFragment)
                add(R.id.fragment_container, featureContainerFragment, "3").hide(featureContainerFragment)
                add(R.id.fragment_container, servicesFragment, "2").hide(servicesFragment)
                add(R.id.fragment_container, homeFragment, "1")
            }.commit()
        }

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    switchFragment(homeFragment, getString(R.string.title_main))
                    true
                }
                R.id.nav_services -> {
                    switchFragment(servicesFragment, getString(R.string.title_detail))
                    true
                }
                R.id.nav_features -> {
                    switchFragment(featureContainerFragment, getString(R.string.sub_frag_title))
                    true
                }
                R.id.nav_profile -> {
                    switchFragment(profileFragment, getString(R.string.title_developer))
                    true
                }
                else -> false
            }
        }
    }

    private fun switchFragment(targetFragment: Fragment, title: String) {
        if (activeFragment != targetFragment) {
            supportFragmentManager.beginTransaction()
                .hide(activeFragment)
                .show(targetFragment)
                .commit()
            activeFragment = targetFragment
            supportActionBar?.title = title
        }
    }
}