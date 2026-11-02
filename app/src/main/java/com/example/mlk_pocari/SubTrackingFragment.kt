package com.example.mlk_pocari

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class SubTrackingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_sub_tracking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnTrackingRating = view.findViewById<Button>(R.id.btnTrackingRating)
        btnTrackingRating?.setOnClickListener {
            val intent = Intent(requireContext(), RatingActivity::class.java)
            startActivity(intent)
        }
    }
}