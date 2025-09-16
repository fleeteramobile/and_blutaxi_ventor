package com.blueventor.repository

import com.blueventor.network.ApiService
import com.blueventor.network.UiState
import com.blueventor.network.request.RequestDriverDetails
import com.blueventor.network.request.RequestTripList
import com.blueventor.network.request.Requestloginaccess
import com.blueventor.network.response.RespondeLoginAccess
import com.blueventor.network.response.ResponseDriverDetails
import com.blueventor.network.response.ResponseTripList
import javax.inject.Inject

class TripListRepository @Inject constructor(val apiService: ApiService) {

    suspend fun getTripList(requestTripList: RequestTripList): UiState<ResponseTripList>
    {
        return try {

            val response = apiService.getTripList(requestTripList)
            if (response != null && response.trip_details.status ==1)
            {
                UiState.Success(response!!)

            }
            else{
                UiState.Error(response.trip_details.message)

            }
        }catch (e: Exception)
        {
            UiState.Error(e.localizedMessage ?: "An unexpected error occurred")
        }
    }
}