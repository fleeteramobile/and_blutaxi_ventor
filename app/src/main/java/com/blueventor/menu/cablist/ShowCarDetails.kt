package com.blueventor.menu.cablist

import com.blueventor.network.response.ResponseCarList
import com.blueventor.network.response.ResponseCarPerformance

interface ShowCarDetails {
    fun showCarDetails(data : ResponseCarList.Data)


}