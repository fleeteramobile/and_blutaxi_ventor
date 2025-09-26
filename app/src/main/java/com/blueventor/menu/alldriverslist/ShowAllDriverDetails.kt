package com.blueventor.menu.alldriverslist

import com.blueventor.network.response.ResponseCarPerformance

interface ShowAllDriverDetails {
    fun showDriverDetails(data : ResponseAllDriverList.Data)
    fun callDriver(responseData: ResponseAllDriverList.Data)

}