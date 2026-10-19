package com.example.mlk_pocari

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton

class FeatureContainerFragment : Fragment() {

    private lateinit var btnSubKiloan: MaterialButton
    private lateinit var btnSubSatuan: MaterialButton
    private lateinit var btnSubTracking: MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_feature_container, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnSubKiloan = view.findViewById(R.id.btnSubKiloan)
        btnSubSatuan = view.findViewById(R.id.btnSubSatuan)
        btnSubTracking = view.findViewById(R.id.btnSubTracking)

        // Default sub-fragment on start
        loadSubFragment(SubKiloanFragment())
        highlightActiveButton(btnSubKiloan)

        btnSubKiloan.setOnClickListener {
            loadSubFragment(SubKiloanFragment())
            highlightActiveButton(btnSubKiloan)
        }

        btnSubSatuan.setOnClickListener {
            loadSubFragment(SubSatuanFragment())
            highlightActiveButton(btnSubSatuan)
        }

        btnSubTracking.setOnClickListener {
            loadSubFragment(SubTrackingFragment())
            highlightActiveButton(btnSubTracking)
        }
    }

    private fun loadSubFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.sub_fragment_container, fragment)
            .commit()
    }

    private fun highlightActiveButton(activeBtn: MaterialButton) {
        val context = context ?: return
        val tealPrimary = ContextCompat.getColor(context, R.color.qcuci_teal_primary)
        val white = ContextCompat.getColor(context, R.color.white)

        val allButtons = listOf(btnSubKiloan, btnSubSatuan, btnSubTracking)

        allButtons.forEach { btn ->
            if (btn == activeBtn) {
                btn.setBackgroundColor(tealPrimary)
                btn.setTextColor(white)
            } else {
                btn.setBackgroundColor(white)
                btn.setTextColor(tealPrimary)
            }
        }
    }
}