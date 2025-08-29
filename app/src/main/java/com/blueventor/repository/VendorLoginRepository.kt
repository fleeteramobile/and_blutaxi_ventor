package com.blueventor.repository

import com.blueventor.network.ApiService
import com.blueventor.network.UiState
import com.blueventor.network.request.Requestloginaccess
import com.blueventor.network.response.RespondeLoginAccess
import javax.inject.Inject

class VendorLoginRepository @Inject constructor(val apiService: ApiService) {

    suspend fun loginAccess(requestloginaccess: Requestloginaccess): UiState<RespondeLoginAccess>
    {
        return try {

            val response = apiService.vendorLogin(requestloginaccess)
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