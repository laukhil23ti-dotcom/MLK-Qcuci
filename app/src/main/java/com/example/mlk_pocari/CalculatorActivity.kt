package com.example.mlk_pocari

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.card.MaterialCardView
import java.text.NumberFormat
import java.util.Locale

class CalculatorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbarCalc)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        toolbar.setNavigationOnClickListener {
            finish()
        }

        val rgServices = findViewById<RadioGroup>(R.id.rgServices)
        val etAmount = findViewById<EditText>(R.id.etAmount)
        val cbParfum = findViewById<CheckBox>(R.id.cbParfum)
        val cbSoftener = findViewById<CheckBox>(R.id.cbSoftener)
        val cbAntiNoda = findViewById<CheckBox>(R.id.cbAntiNoda)
        val btnCalculate = findViewById<Button>(R.id.btnCalculate)

        val cardResult = findViewById<MaterialCardView>(R.id.cardResult)
        val tvResultService = findViewById<TextView>(R.id.tvResultService)
        val tvResultAddons = findViewById<TextView>(R.id.tvResultAddons)
        val tvResultTotal = findViewById<TextView>(R.id.tvResultTotal)

        btnCalculate.setOnClickListener {
            val amountStr = etAmount.text.toString().trim()
            if (amountStr.isEmpty()) {
                Toast.makeText(this, getString(R.string.calc_empty_warning), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val amount = amountStr.toDoubleOrNull()
            if ((amount == null) || (amount <= 0)) {
                Toast.makeText(this, getString(R.string.calc_empty_warning), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val (serviceName, unitPrice, unitLabel) = when (rgServices.checkedRadioButtonId) {
                R.id.rbExpress -> Triple("Cuci Kiloan Express", 10000.0, "kg")
                R.id.rbDryClean -> Triple("Dry Cleaning / Satuan", 15000.0, "item")
                R.id.rbBedcover -> Triple("Cuci Bedcover", 25000.0, "item")
                else -> Triple("Cuci Kiloan Reguler", 6000.0, "kg")
            }

            var addonsTotal = 0.0
            val addonList = mutableListOf<String>()

            if (cbParfum.isChecked) {
                addonsTotal += 2000.0
                addonList.add("Extra Parfum (+Rp 2.000)")
            }
            if (cbSoftener.isChecked) {
                addonsTotal += 3000.0
                addonList.add("Softener (+Rp 3.000)")
            }
            if (cbAntiNoda.isChecked) {
                addonsTotal += 5000.0
                addonList.add("Anti Noda (+Rp 5.000)")
            }

            val baseTotal = amount * unitPrice
            val grandTotal = baseTotal + addonsTotal

            val localeID = Locale.Builder().setLanguage("id").setRegion("ID").build()
            val rupiahFormat = NumberFormat.getCurrencyInstance(localeID)
            val formattedTotal = rupiahFormat.format(grandTotal)

            val displayAmount = if ((amount % 1.0) == 0.0) {
                amount.toInt().toString()
            } else {
                amount.toString()
            }

            tvResultService.text = getString(R.string.result_service_format, serviceName, displayAmount, unitLabel)

            val addonsText = if (addonList.isNotEmpty()) {
                addonList.joinToString(", ")
            } else {
                getString(R.string.result_addons_none)
            }
            tvResultAddons.text = getString(R.string.result_addons_format, addonsText)

            tvResultTotal.text = formattedTotal

            cardResult.visibility = View.VISIBLE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}