package com.blueventor.menu.cablist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.blueventor.R
import com.blueventor.menu.driver.alldriverslist.ShowAllDriverDetails
import com.blueventor.network.response.ResponseCarList
import com.blueventor.network.response.ResponseCarPerformance
import com.blueventor.util.onclik

class CabListAdapter(
    val showCarDetails: ShowCarDetails,
    private val driverList: List<ResponseCarList.Data>
) :
    RecyclerView.Adapter<CabListAdapter.DriverViewHolder>() {

    inner class DriverViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvType: TextView = itemView.findViewById(R.id.tvType)
        val tvCarName: TextView = itemView.findViewById(R.id.tvCarName)
        val tvCarNumber: TextView = itemView.findViewById(R.id.tvCarNumber)
        val car_lay_main: CardView = itemView.findViewById(R.id.car_lay_main)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DriverViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cab_list_item, parent, false)
        return DriverViewHolder(view)
    }

    override fun getItemCount(): Int {
        return driverList.size
    }

    override fun onBindViewHolder(holder: DriverViewHolder, position: Int) {
        val driver = driverList[position]
        holder.tvType.text = "Type : ${driver.taxi_model}"
        holder.tvCarName.text = "${driver.taxi_manufacturer}"
        holder.tvCarNumber.text = "${driver.taxi_no.toString()}"
        holder.car_lay_main.onclik {
            showCarDetails.showCarDetails(driver)
        }

    }


}
