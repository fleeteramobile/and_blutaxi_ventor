package com.blueventor.network.repository

import com.blueventor.network.ApiService
import com.blueventor.network.UiState
import com.blueventor.network.request.RequestDashBoardCarPerformanceDetails
import com.blueventor.network.request.RequestDashBoardDetails
import com.blueventor.network.response.ResponseCarPerformance
import com.blueventor.network.response.ResponseDashBoardDetails
import javax.inject.Inject

class DashBoardRepository @Inject constructor(val apiService: ApiService) {
    suspend fun getDashBoardDetails(requestDashBoardDetails: RequestDashBoardDetails):UiState<ResponseDashBoardDetails>
    {
        return try {
            val response = apiService.getDashBoardDetails(requestDashBoardDetails)
            if (response!=null && response.status==1)
            {
                UiState.Success(response!!)
            }
            else
            {
                UiState.Error(response.message!!)
            }


        }catch (e: Exception)
        {
            UiState.Error(e.localizedMessage ?: "An unexpected error occurred")

        }
    }


    suspend fun getDashBoardCarPerformance(requestDashBoardCarPerformanceDetails: RequestDashBoardCarPerformanceDetails):UiState<ResponseCarPerformance>
    {
        return try {
            val response = apiService.getCarPerformanceDetails(requestDashBoardCarPerformanceDetails)
            if (response!=null && response.status==1)
            {
                UiState.Success(response!!)
            }
            else
            {
                UiState.Error(response.message!!)
            }


        }catch (e: Exception)
        {
            UiState.Error(e.localizedMessage ?: "An unexpected error occurred")

        }
    }
}