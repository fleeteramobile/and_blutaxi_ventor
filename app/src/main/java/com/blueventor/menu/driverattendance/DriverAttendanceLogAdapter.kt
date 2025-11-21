package com.blueventor.menu.driverattendance

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
import com.blueventor.network.response.ResponseDriverAttendanceDetails
import com.blueventor.network.response.ResponseDriverWalletLogs
import com.blueventor.network.response.ResponseDriverWalletRecharge

import com.blueventor.util.hideView
import com.blueventor.util.onclik
import com.blueventor.util.setImageOnclick
import com.blueventor.util.setOnclick
import java.text.SimpleDateFormat
import java.util.Locale

class DriverAttendanceLogAdapter(

    private val driverList: List<ResponseDriverAttendanceDetails.Details.AttendanceDetail>
) :
    RecyclerView.Adapter<DriverAttendanceLogAdapter.DriverViewHolder>() {

    inner class DriverViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtName: TextView = itemView.findViewById(R.id.txtName)
        val txtStatus: TextView = itemView.findViewById(R.id.txtStatus)


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DriverViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_absentee, parent, false)
        return DriverViewHolder(view)
    }

    override fun getItemCount(): Int {
        return driverList.size
    }

    override fun onBindViewHolder(holder: DriverViewHolder, position: Int) {
        val driver = driverList[position]


        holder.txtName.text = driver.date
        holder.txtStatus.text = driver.login_hours




    }


}
