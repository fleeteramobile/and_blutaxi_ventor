package com.blueventor.network.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blueventor.network.UiState
import com.blueventor.network.request.RequestDashBoardCarPerformanceDetails
import com.blueventor.network.request.RequestDashBoardDetails
import com.blueventor.network.response.ResponseCarPerformance
import com.blueventor.network.response.ResponseCheckCompanyDomain
import com.blueventor.network.response.ResponseDashBoardDetails
import com.blueventor.network.repository.DashBoardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(val dashBoardRepository: DashBoardRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<ResponseDashBoardDetails>>(UiState.Idle)
    val uiState: StateFlow<UiState<ResponseDashBoardDetails>> = _uiState

    private val _uiStateCarPerformance = MutableStateFlow<UiState<ResponseCarPerformance>>(UiState.Idle)
    val uiStateCarPerformance: StateFlow<UiState<ResponseCarPerformance>> = _uiStateCarPerformance
    suspend fun getDashBoardDetails( requestDashBoardDetails: RequestDashBoardDetails)
    {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            _uiState.value = dashBoardRepository.getDashBoardDetails(requestDashBoardDetails)
        }
    }

    suspend fun getDashBoardCarPerformanceDetails( requestDashBoardCarPerformanceDetails: RequestDashBoardCarPerformanceDetails)
    {
        viewModelScope.launch {
            _uiStateCarPerformance.value = UiState.Loading
            _uiStateCarPerformance.value = dashBoardRepository.getDashBoardCarPerformance(requestDashBoardCarPerformanceDetails)
        }
    }

}