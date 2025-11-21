package com.blueventor.menu.driver.driverlist

import com.blueventor.network.response.ResponseCarPerformance

interface ShowDriverDetails {
    fun showDriverDetails(data : ResponseCarPerformance.Data)
    fun callDriver(responseData: ResponseCarPerformance.Data)
    fun callMessage(responseData: ResponseCarPerformance.Data)

}