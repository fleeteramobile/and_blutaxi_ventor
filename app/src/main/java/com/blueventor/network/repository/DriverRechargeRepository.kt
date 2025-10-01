package com.blueventor.network.repository

import ResponseAllDriverList
import com.blueventor.network.ApiService
import com.blueventor.network.UiState
import com.blueventor.network.request.RequestDriverRecharge
import com.blueventor.network.request.RequestGetAllDriverList
import com.blueventor.network.request.Requestloginaccess
import com.blueventor.network.response.RespondeLoginAccess
import com.blueventor.network.response.ResponseDriverWalletRecharge

import com.blueventor.util.logDebugMessage
import javax.inject.Inject


class DriverRechargeRepository @Inject constructor(val apiService: ApiService) {

    suspend fun addWalletAmount(requestDriverRecharge: RequestDriverRecharge): UiState<ResponseDriverWalletRecharge>
    {
        logDebugMessage("state_result","5")

        return try {

            val response = apiService.addAmountDriverWallet(requestDriverRecharge)
            logDebugMessage("state_result","7")

            if (response != null && response.status ==1)
            {
                logDebugMessage("state_result","2")

                UiState.Success(response!!)

            }
            else{
                logDebugMessage("state_result","3")

                UiState.Error(response.message)

            }
        }catch (e: Exception)
        {
            logDebugMessage("state_result",e.localizedMessage)

            UiState.Error(e.localizedMessage ?: "An unexpected error occurred")
        }
    }
}