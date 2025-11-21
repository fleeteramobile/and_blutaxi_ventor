package com.blueventor.menu.driverattendance

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.blueventor.R

class MonthAdapter(
    private val list: MutableList<MonthModel>,
    private val callback: (MonthModel) -> Unit
) : RecyclerView.Adapter<MonthAdapter.MonthVH>() {

    inner class MonthVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtMonth = itemView.findViewById<TextView>(R.id.txtMonthName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthVH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_date, parent, false)
        return MonthVH(v)
    }

    override fun onBindViewHolder(holder: MonthVH, position: Int) {
        val item = list[position]

        holder.txtMonth.text = item.name
        holder.txtMonth.background = ContextCompat.getDrawable(
            holder.itemView.context,
            if (item.isSelected) R.drawable.bg_month_selected
            else R.drawable.bg_month_normal
        )

        holder.itemView.setOnClickListener {
            list.forEach { it.isSelected = false }
            item.isSelected = true
            notifyDataSetChanged()
            callback(item)
        }
    }

    override fun getItemCount() = list.size
}
