package com.blueventor.network.repository

import com.blueventor.network.ApiService
import com.blueventor.network.UiState
import com.blueventor.network.request.RequestDriverDetails
import com.blueventor.network.request.Requestloginaccess
import com.blueventor.network.response.RespondeLoginAccess
import com.blueventor.network.response.ResponseDriverDetails
import com.blueventor.util.logDebugMessage
import javax.inject.Inject

class DriverDetailsRepository @Inject constructor(val apiService: ApiService) {

    suspend fun getDriverDetails(requestDriverDetails: RequestDriverDetails): UiState<ResponseDriverDetails>
    {
        return try {

            val response = apiService.getDriverDetails(requestDriverDetails)
            if (response != null && response.status ==1)
            {
                UiState.Success(response!!)

            }
            else{
                UiState.Error(response.message)

            }
        }catch (e: Exception)
        {
            logDebugMessage("getdriverdetails",e.localizedMessage ?: "An unexpected error occurred")

            UiState.Error(e.localizedMessage ?: "An unexpected error occurred")
        }
    }
}