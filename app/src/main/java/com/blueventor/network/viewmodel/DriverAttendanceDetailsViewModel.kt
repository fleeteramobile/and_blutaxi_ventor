package com.blueventor.network.viewmodel

import ResponseAllDriverList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blueventor.network.NetworkModule_ProvideBaseUrlFactory
import com.blueventor.network.UiState
import com.blueventor.network.request.RequestDriverDetails
import com.blueventor.network.request.RequestGetAllDriverList

import com.blueventor.network.repository.AllDriverRepository
import com.blueventor.network.repository.DriverAttendanceDetailsRepository
import com.blueventor.network.repository.DriverRechargeLogRepository
import com.blueventor.network.request.RequestDriverAttendanceDetails
import com.blueventor.network.request.RequestDriverRechargeLogs
import com.blueventor.network.response.ResponseDriverAttendanceDetails
import com.blueventor.network.response.ResponseDriverWalletLogs
import com.blueventor.util.logDebugMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Thread.State
import javax.inject.Inject
@HiltViewModel
class DriverAttendanceDetailsViewModel @Inject constructor(val driverAttendanceDetailsRepository: DriverAttendanceDetailsRepository): ViewModel(){
private val _uiState = MutableStateFlow<UiState<ResponseDriverAttendanceDetails>>(UiState.Idle)
val uiState: StateFlow<UiState<ResponseDriverAttendanceDetails>> = _uiState

    fun getDriverDetails(requestDriverAttendanceDetails: RequestDriverAttendanceDetails) {
        logDebugMessage("state_result","1")

        viewModelScope.launch {
            _uiState.value = UiState.Loading
            _uiState.value = driverAttendanceDetailsRepository.getDriverRechargeLogs(requestDriverAttendanceDetails)
        }


    }
}