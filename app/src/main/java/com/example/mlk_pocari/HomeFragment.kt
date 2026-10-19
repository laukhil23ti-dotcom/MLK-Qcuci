package com.example.mlk_pocari

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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

        val btnMyProject = view.findViewById<Button>(R.id.btnMyProject)
        val btnCalculator = view.findViewById<Button>(R.id.btnCalculator)
        val btnWebsite = view.findViewById<Button>(R.id.btnWebsite)

        btnMyProject.setOnClickListener {
            val intent = Intent(requireContext(), DetailActivity::class.java)
            startActivity(intent)
        }

        btnCalculator.setOnClickListener {
            val intent = Intent(requireContext(), CalculatorActivity::class.java)
            startActivity(intent)
        }

        btnWebsite.setOnClickListener {
            val intent = Intent(requireContext(), WebActivity::class.java)
            startActivity(intent)
        }
    }
}