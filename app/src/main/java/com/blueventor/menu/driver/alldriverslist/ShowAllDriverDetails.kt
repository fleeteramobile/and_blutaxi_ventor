package com.blueventor.menu.driver.alldriverslist

import com.blueventor.network.response.ResponseCarPerformance

interface ShowAllDriverDetails {
    fun showDriverDetails(data : ResponseAllDriverList.Data)
    fun callDriver(responseData: ResponseAllDriverList.Data)

}