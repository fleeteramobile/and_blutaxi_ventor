package com.blueventor.menu.driver.driverlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.blueventor.R
import com.blueventor.network.response.ResponseCarPerformance
import com.blueventor.util.onclik
import com.blueventor.util.setImageOnclick

class DriverListAdapter(val showDriverDetails: ShowDriverDetails, private val driverList: List<ResponseCarPerformance.Data>) :
    RecyclerView.Adapter<DriverListAdapter.DriverViewHolder>() {

    inner class DriverViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val car_plate: TextView = itemView.findViewById(R.id.car_plate)

        val driver_name: TextView = itemView.findViewById(R.id.driver_name)
        val taxi_manufacturer: TextView = itemView.findViewById(R.id.taxi_manufacturer)
        val model_name: TextView = itemView.findViewById(R.id.model_name)
        val driver_main_lay: CardView = itemView.findViewById(R.id.driver_main_lay)
        val btnCall: ImageView = itemView.findViewById(R.id.btnCall)
        val btnMessage: ImageView = itemView.findViewById(R.id.btnMessage)
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

        holder.model_name.text = "${driver.model_name}"
        holder.taxi_manufacturer.text = "${driver.taxi_manufacturer}"
        holder.driver_name.text = "${driver.driver_name.toString()}"

        holder.driver_main_lay.onclik {
            showDriverDetails.showDriverDetails(driver)
        }
        setImageOnclick(holder.btnCall)
        {
            showDriverDetails.callDriver(driver)
        }

        setImageOnclick(holder.btnMessage)
        {
            showDriverDetails.callMessage(driver)
        }
//        holder.call_button.setOnClickListener {
//            showDriverDetails.callDriver(driver)
//        }

    }



}
