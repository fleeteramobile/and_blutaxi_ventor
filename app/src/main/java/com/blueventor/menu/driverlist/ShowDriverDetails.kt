package com.blueventor.menu.driverlist

import com.blueventor.network.response.ResponseCarPerformance

interface ShowDriverDetails {
    fun showDriverDetails(data : ResponseCarPerformance.Data)
    fun callDriver(responseData: ResponseCarPerformance.Data)

}