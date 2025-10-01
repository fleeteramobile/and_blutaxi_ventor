package com.blueventor.network.viewmodel

import ResponseAllDriverList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blueventor.network.NetworkModule_ProvideBaseUrlFactory
import com.blueventor.network.UiState
import com.blueventor.network.request.RequestDriverDetails
import com.blueventor.network.request.RequestGetAllDriverList

import com.blueventor.network.repository.AllDriverRepository
import com.blueventor.network.repository.CarListRepository
import com.blueventor.network.request.RequestGetDriverLocation
import com.blueventor.network.response.ResponseCarList
import com.blueventor.util.logDebugMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Thread.State
import javax.inject.Inject
@HiltViewModel
class CarListListViewModel @Inject constructor(val carListRepository: CarListRepository): ViewModel(){
private val _uiState = MutableStateFlow<UiState<ResponseCarList>>(UiState.Idle)
val uiState: StateFlow<UiState<ResponseCarList>> = _uiState

    fun getDriverDetails(requestGetDriverLocation: RequestGetDriverLocation) {
        logDebugMessage("state_result","1")

        viewModelScope.launch {
            _uiState.value = UiState.Loading
            _uiState.value = carListRepository.getAllDriverList(requestGetDriverLocation)
        }


    }
}