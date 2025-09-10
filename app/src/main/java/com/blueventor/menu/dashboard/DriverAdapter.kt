package com.blueventor.menu.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blueventor.R
import com.blueventor.network.response.ResponseCarPerformance

class DriverAdapter(private val driverList: List<ResponseCarPerformance.Data>) :
    RecyclerView.Adapter<DriverAdapter.DriverViewHolder>() {

    inner class DriverViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taxiNo: TextView = itemView.findViewById(R.id.tvTaxiNo)
        val price: TextView = itemView.findViewById(R.id.tvPrice)
        val rides: TextView = itemView.findViewById(R.id.tvRides)
        val tvRatingValue: TextView = itemView.findViewById(R.id.tvRatingValue)
        val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)
        val taxiImage: ImageView = itemView.findViewById(R.id.ivTaxi)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DriverViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_driver, parent, false)
        return DriverViewHolder(view)
    }

    override fun getItemCount(): Int {
       return driverList.size
    }

    override fun onBindViewHolder(holder: DriverViewHolder, position: Int) {
        val driver = driverList[position]
        holder.taxiNo.text = driver.taxi_no
        holder.price.text = "₹${driver.total_amount.toInt()}"
        holder.tvRatingValue.text = "${driver.average_rating.toString()}"
        holder.rides.text = "Rides: ${driver.trip_count}"

    }


}
