package com.example.mlk_pocari

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Bind Grid Menu Cards
        val cardKiloan = view.findViewById<CardView>(R.id.cardGridKiloan)
        val cardSatuan = view.findViewById<CardView>(R.id.cardGridSatuan)
        val cardTracking = view.findViewById<CardView>(R.id.cardGridTracking)
        val cardHistory = view.findViewById<CardView>(R.id.cardGridHistory)
        val cardInfo = view.findViewById<CardView>(R.id.cardGridInfo)
        val cardProfile = view.findViewById<CardView>(R.id.cardGridProfile)

        // Bind Quick Action Buttons
        val btnMyProject = view.findViewById<Button>(R.id.btnMyProject)
        val btnCalculator = view.findViewById<Button>(R.id.btnCalculator)
        val btnWebsite = view.findViewById<Button>(R.id.btnWebsite)

        cardKiloan?.setOnClickListener {
            val intent = Intent(requireContext(), DetailActivity::class.java)
            startActivity(intent)
        }

        cardSatuan?.setOnClickListener {
            val intent = Intent(requireContext(), DetailActivity::class.java)
            startActivity(intent)
        }

        cardTracking?.setOnClickListener {
            val intent = Intent(requireContext(), RatingActivity::class.java)
            startActivity(intent)
        }

        cardHistory?.setOnClickListener {
            val intent = Intent(requireContext(), OrderHistoryActivity::class.java)
            startActivity(intent)
        }

        cardInfo?.setOnClickListener {
            val intent = Intent(requireContext(), CalculatorActivity::class.java)
            startActivity(intent)
        }

        cardProfile?.setOnClickListener {
            val intent = Intent(requireContext(), DeveloperActivity::class.java)
            startActivity(intent)
        }

        btnMyProject?.setOnClickListener {
            val intent = Intent(requireContext(), DetailActivity::class.java)
            startActivity(intent)
        }

        btnCalculator?.setOnClickListener {
            val intent = Intent(requireContext(), CalculatorActivity::class.java)
            startActivity(intent)
        }

        btnWebsite?.setOnClickListener {
            val intent = Intent(requireContext(), WebActivity::class.java)
            startActivity(intent)
        }
    }
}