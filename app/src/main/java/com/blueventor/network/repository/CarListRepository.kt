package com.blueventor.network.repository

import ResponseAllDriverList
import com.blueventor.network.ApiService
import com.blueventor.network.UiState
import com.blueventor.network.request.RequestGetAllDriverList
import com.blueventor.network.request.RequestGetDriverLocation
import com.blueventor.network.request.Requestloginaccess
import com.blueventor.network.response.RespondeLoginAccess
import com.blueventor.network.response.ResponseCarList

import com.blueventor.util.logDebugMessage
import javax.inject.Inject


class CarListRepository @Inject constructor(val apiService: ApiService) {

    suspend fun getAllDriverList(requestGetDriverLocation: RequestGetDriverLocation): UiState<ResponseCarList>
    {


        return try {

            val response = apiService.getCompanyWiseCarList(requestGetDriverLocation)


            if (response != null && response.status ==1)
            {


                UiState.Success(response!!)

            }
            else{


                UiState.Error(response.message)

            }
        }catch (e: Exception)
        {

            UiState.Error(e.localizedMessage ?: "An unexpected error occurred")
        }
    }
}