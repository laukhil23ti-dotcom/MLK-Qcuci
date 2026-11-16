package com.example.mlk_pocari

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OrderHistoryAdapter(
    private val orders: List<OrderModel>,
    private val onItemClick: ((OrderModel) -> Unit)? = null,
) : RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvOrderId: TextView = view.findViewById(R.id.tvOrderId)
        val tvOrderStatus: TextView = view.findViewById(R.id.tvOrderStatus)
        val tvServiceName: TextView = view.findViewById(R.id.tvServiceName)
        val tvOrderWeight: TextView = view.findViewById(R.id.tvOrderWeight)
        val tvOrderDate: TextView = view.findViewById(R.id.tvOrderDate)
        val tvOrderPrice: TextView = view.findViewById(R.id.tvOrderPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = orders[position]
        holder.tvOrderId.text = item.orderId
        holder.tvOrderStatus.text = item.status
        holder.tvServiceName.text = item.serviceName
        holder.tvOrderWeight.text = item.weightOrQty
        holder.tvOrderDate.text = item.date
        holder.tvOrderPrice.text = item.totalPrice

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(item)
        }
    }

    override fun getItemCount(): Int = orders.size
}