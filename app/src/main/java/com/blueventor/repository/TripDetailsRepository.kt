package com.blueventor.repository

import com.blueventor.network.ApiService
import com.blueventor.network.UiState
import com.blueventor.network.request.RequestDriverDetails
import com.blueventor.network.request.RequestTripDetails
import com.blueventor.network.request.Requestloginaccess
import com.blueventor.network.response.RespondeLoginAccess
import com.blueventor.network.response.ResponseDriverDetails
import com.blueventor.network.response.ResponseTripDetails
import javax.inject.Inject

class TripDetailsRepository @Inject constructor(val apiService: ApiService) {

    suspend fun getDriverDetails(requestTripDetails: RequestTripDetails): UiState<ResponseTripDetails>
    {
        return try {

            val response = apiService.getTripDetails(requestTripDetails)
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