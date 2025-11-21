package com.blueventor.menu.driverrechargelogs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.blueventor.R
import com.blueventor.menu.driver.alldriverslist.ShowAllDriverDetails
import com.blueventor.network.response.ResponseDriverWalletLogs
import com.blueventor.network.response.ResponseDriverWalletRecharge

import com.blueventor.util.hideView
import com.blueventor.util.onclik
import com.blueventor.util.setImageOnclick
import com.blueventor.util.setOnclick
import java.text.SimpleDateFormat
import java.util.Locale

class DriverRechargeLogAdapter(

    private val driverList: List<ResponseDriverWalletLogs.Data.Record>
) :
    RecyclerView.Adapter<DriverRechargeLogAdapter.DriverViewHolder>() {

    inner class DriverViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txnName: TextView = itemView.findViewById(R.id.txnName)
        val txnDate: TextView = itemView.findViewById(R.id.txnDate)
        val txnAmount: TextView = itemView.findViewById(R.id.txnAmount)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DriverViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.driver_rechare_logs_item, parent, false)
        return DriverViewHolder(view)
    }

    override fun getItemCount(): Int {
        return driverList.size
    }

    override fun onBindViewHolder(holder: DriverViewHolder, position: Int) {
        val driver = driverList[position]


        holder.txnName.text = formatRechargeBy(driver.recharge_by)
        holder.txnDate.text = formatDate(driver.createdate)
        holder.txnAmount.text = "₹${driver.amount}"



    }
    private fun formatRechargeBy(rechargeBy: String?): String {
        return if (rechargeBy == "Admin") {
            "Recharge added by Admin"
        } else {
            "Recharge added by Vendor"
        }
    }

    private fun formatDate(input: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.getDefault())

            val date = inputFormat.parse(input)
            outputFormat.format(date!!)
        } catch (e: Exception) {
            input
        }
    }

}
