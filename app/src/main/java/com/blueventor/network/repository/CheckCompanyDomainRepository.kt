package com.blueventor.network.repository

import android.annotation.SuppressLint
import com.blueventor.network.ApiService
import com.blueventor.network.UiState
import com.blueventor.network.request.RequestCheckCompanyDomain
import com.blueventor.network.response.ResponseCheckCompanyDomain
import javax.inject.Inject

class CheckCompanyDomainRepository @Inject constructor(private val apiService: ApiService) {
    @SuppressLint("SuspiciousIndentation")
    suspend fun checkCompany(request: RequestCheckCompanyDomain): UiState<ResponseCheckCompanyDomain>
    {
        return try {
            val response = apiService.checkCompanyDomain(request)
            if (response.status ==1  && response != null) {
                UiState.Success(response!!)
            } else {
                UiState.Error(response!!.message)
            }
        } catch (e: Exception) {
            UiState.Error(e.localizedMessage ?: "An unexpected error occurred")
        }
    }



}