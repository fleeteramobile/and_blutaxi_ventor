package com.blueventor.menu.driver.driverlist

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
import com.blueventor.network.response.ResponseCarPerformance

class DriverListAdapter(val showDriverDetails: ShowDriverDetails, private val driverList: List<ResponseCarPerformance.Data>) :
    RecyclerView.Adapter<DriverListAdapter.DriverViewHolder>() {

    inner class DriverViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val car_plate: TextView = itemView.findViewById(R.id.car_plate)
        val rating: TextView = itemView.findViewById(R.id.rating)
        val driver_name: TextView = itemView.findViewById(R.id.driver_name)
        val price: TextView = itemView.findViewById(R.id.price)
        val driver_main_lay: ConstraintLayout = itemView.findViewById(R.id.driver_main_lay)
        val call_button: ImageView = itemView.findViewById(R.id.call_button)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DriverViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.driver_list_item, parent, false)
        return DriverViewHolder(view)
    }

    override fun getItemCount(): Int {
       return driverList.size
    }

    override fun onBindViewHolder(holder: DriverViewHolder, position: Int) {
        val driver = driverList[position]
        holder.car_plate.text = driver.taxi_no
        holder.rating.text = "${driver.average_rating.toInt()}"
        holder.price.text = "₹${driver.total_amount.toInt()}"
        holder.driver_name.text = "${driver.driver_name.toString()}"
        holder.driver_main_lay.setOnClickListener {
            showDriverDetails.showDriverDetails(driver)
        }
        holder.call_button.setOnClickListener {
            showDriverDetails.callDriver(driver)
        }

    }



}
