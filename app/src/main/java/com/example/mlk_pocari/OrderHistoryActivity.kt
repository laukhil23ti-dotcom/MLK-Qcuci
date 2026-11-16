package com.example.mlk_pocari

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar

class OrderHistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_history)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbarHistory)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        toolbar.setNavigationOnClickListener {
            finish()
        }

        val rvOrderHistory = findViewById<RecyclerView>(R.id.rvOrderHistory)
        rvOrderHistory.layoutManager = LinearLayoutManager(this)

        val historyList = listOf(
            OrderModel(
                orderId = "#QC-88921",
                serviceName = "Cuci Kiloan Express",
                weightOrQty = "Berat: 4.5 kg • Extra Parfum",
                totalPrice = "Rp 47.000",
                date = "02 Nov 2026",
                status = "Selesai",
            ),
            OrderModel(
                orderId = "#QC-88910",
                serviceName = "Dry Cleaning Satuan",
                weightOrQty = "2 Jas Blazer • 1 Bedcover Large",
                totalPrice = "Rp 70.000",
                date = "29 Oct 2026",
                status = "Selesai",
            ),
            OrderModel(
                orderId = "#QC-88895",
                serviceName = "Cuci Kiloan Reguler",
                weightOrQty = "Berat: 6.0 kg • Pelembut Softener",
                totalPrice = "Rp 39.000",
                date = "25 Oct 2026",
                status = "Selesai",
            ),
            OrderModel(
                orderId = "#QC-88870",
                serviceName = "Cuci Bedcover & Selimut",
                weightOrQty = "Jumlah: 2 Bedcover King Size",
                totalPrice = "Rp 50.000",
                date = "20 Oct 2026",
                status = "Selesai",
            ),
            OrderModel(
                orderId = "#QC-88842",
                serviceName = "Cuci Sepatu & Sneaker",
                weightOrQty = "2 Pasang Sneaker White Care",
                totalPrice = "Rp 70.000",
                date = "15 Oct 2026",
                status = "Selesai",
            ),
        )

        val adapter = OrderHistoryAdapter(historyList) { order ->
            Toast.makeText(this, "Nota ${order.orderId}: ${order.serviceName}", Toast.LENGTH_SHORT).show()
        }
        rvOrderHistory.adapter = adapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}