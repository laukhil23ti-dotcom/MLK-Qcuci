package com.example.mlk_pocari

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mlk_pocari.data.api.SupabaseClient
import com.example.mlk_pocari.data.model.OrderData
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import retrofit2.HttpException

class OrderHistoryActivity : AppCompatActivity() {

    private lateinit var prefManager: PrefManager
    private lateinit var rvOrderHistory: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvEmpty: TextView
    private lateinit var fabAddOrder: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_history)

        prefManager = PrefManager(this)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbarHistory)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        toolbar.setNavigationOnClickListener { finish() }

        rvOrderHistory = findViewById(R.id.rvOrderHistory)
        progressBar = findViewById(R.id.progressBarOrders)
        tvEmpty = findViewById(R.id.tvEmptyOrders)
        fabAddOrder = findViewById(R.id.fabAddOrder)

        rvOrderHistory.layoutManager = LinearLayoutManager(this)

        fabAddOrder.setOnClickListener {
            showOrderFormDialog(null)
        }

        loadOrders()
    }

    private fun loadOrders() {
        progressBar.visibility = View.VISIBLE
        tvEmpty.visibility = View.GONE

        lifecycleScope.launch {
            try {
                val orders = SupabaseClient.instance.getOrders()
                progressBar.visibility = View.GONE

                if (orders.isEmpty()) {
                    tvEmpty.visibility = View.VISIBLE
                    rvOrderHistory.adapter = OrderHistoryAdapter(emptyList())
                } else {
                    tvEmpty.visibility = View.GONE
                    val adapter = OrderHistoryAdapter(
                        orders = orders,
                        onItemClick = { order -> showOrderFormDialog(order) },
                        onItemLongClick = { order -> showDeleteConfirmDialog(order) },
                    )
                    rvOrderHistory.adapter = adapter
                }
            } catch (e: HttpException) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@OrderHistoryActivity, "Error (${e.code()}): ${e.message()}", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@OrderHistoryActivity, "Gagal memuat data: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showOrderFormDialog(orderToEdit: OrderData?) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_order_form, null)
        val etServiceName = dialogView.findViewById<EditText>(R.id.etDialogServiceName)
        val etWeightQty = dialogView.findViewById<EditText>(R.id.etDialogWeightQty)
        val etTotalPrice = dialogView.findViewById<EditText>(R.id.etDialogTotalPrice)
        val etStatus = dialogView.findViewById<EditText>(R.id.etDialogStatus)

        if (orderToEdit != null) {
            etServiceName.setText(orderToEdit.serviceName)
            etWeightQty.setText(orderToEdit.weightQty)
            etTotalPrice.setText(orderToEdit.totalPrice.toString())
            etStatus.setText(orderToEdit.status)
        } else {
            etStatus.setText("Diproses")
        }

        val dialogTitle = if (orderToEdit == null) "Tambah Pesanan Baru" else "Edit Pesanan #${orderToEdit.id}"

        AlertDialog.Builder(this)
            .setTitle(dialogTitle)
            .setView(dialogView)
            .setPositiveButton("Simpan") { dialog, _ ->
                val serviceName = etServiceName.text.toString().trim()
                val weightQty = etWeightQty.text.toString().trim()
                val priceStr = etTotalPrice.text.toString().trim()
                val status = etStatus.text.toString().trim()

                if (serviceName.isEmpty() || weightQty.isEmpty() || priceStr.isEmpty()) {
                    Toast.makeText(this, "Mohon lengkapi semua data!", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val totalPrice = priceStr.toLongOrNull() ?: 0L
                val currentUserId = if (prefManager.userId > 0) prefManager.userId else 1L

                if (orderToEdit == null) {
                    // CREATE
                    val newOrder = OrderData(
                        userId = currentUserId,
                        serviceName = serviceName,
                        weightQty = weightQty,
                        totalPrice = totalPrice,
                        status = status.ifEmpty { "Diproses" },
                    )
                    createOrder(newOrder)
                } else {
                    // UPDATE
                    val updatedOrder = OrderData(
                        userId = orderToEdit.userId,
                        serviceName = serviceName,
                        weightQty = weightQty,
                        totalPrice = totalPrice,
                        status = status.ifEmpty { "Diproses" },
                    )
                    updateOrder(orderToEdit.id ?: 0L, updatedOrder)
                }
                dialog.dismiss()
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun createOrder(order: OrderData) {
        lifecycleScope.launch {
            try {
                SupabaseClient.instance.addOrder(order)
                Toast.makeText(this@OrderHistoryActivity, "Pesanan berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
                loadOrders()
            } catch (e: Exception) {
                Toast.makeText(this@OrderHistoryActivity, "Gagal menambah pesanan: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun updateOrder(id: Long, order: OrderData) {
        lifecycleScope.launch {
            try {
                SupabaseClient.instance.updateOrder("eq.$id", order)
                Toast.makeText(this@OrderHistoryActivity, "Pesanan berhasil diperbarui!", Toast.LENGTH_SHORT).show()
                loadOrders()
            } catch (e: Exception) {
                Toast.makeText(this@OrderHistoryActivity, "Gagal memperbarui pesanan: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showDeleteConfirmDialog(order: OrderData) {
        AlertDialog.Builder(this)
            .setTitle("Hapus Pesanan")
            .setMessage("Apakah Anda yakin ingin menghapus pesanan #${order.id}?")
            .setPositiveButton("Hapus") { _, _ ->
                deleteOrder(order.id ?: 0L)
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun deleteOrder(id: Long) {
        lifecycleScope.launch {
            try {
                SupabaseClient.instance.deleteOrder("eq.$id")
                Toast.makeText(this@OrderHistoryActivity, "Pesanan berhasil dihapus!", Toast.LENGTH_SHORT).show()
                loadOrders()
            } catch (e: Exception) {
                Toast.makeText(this@OrderHistoryActivity, "Gagal menghapus pesanan: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            }
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