package com.blueventor.menu.triplist

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
import com.blueventor.menu.driverlist.ShowDriverDetails
import com.blueventor.network.response.ResponseCarPerformance
import com.blueventor.network.response.ResponseTripList

class TripListAdapter(val showTripDetails: ShowTripDetails, private val driverList: List<ResponseTripList.TripDetails.Result>) :
    RecyclerView.Adapter<TripListAdapter.DriverViewHolder>() {

    inner class DriverViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val date_trip: TextView = itemView.findViewById(R.id.date_trip)
        val trip_id: TextView = itemView.findViewById(R.id.trip_id)
        val tvPickup: TextView = itemView.findViewById(R.id.tvPickup)
        val tvDrop: TextView = itemView.findViewById(R.id.tvDrop)
        val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DriverViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.trip_list_item, parent, false)
        return DriverViewHolder(view)
    }

    override fun getItemCount(): Int {
       return driverList.size
    }

    override fun onBindViewHolder(holder: DriverViewHolder, position: Int) {
        val driver = driverList[position]
        holder.date_trip.text = driver.pickup_time
        holder.trip_id.text = "${driver._id.toInt()}"
        holder.tvPickup.text = "${driver.drop_location}"
        holder.tvDrop.text = "${driver.drop_location.toString()}"
        holder.tvPrice.text = "₹ ${driver.amt.toString()}"


    }



}
