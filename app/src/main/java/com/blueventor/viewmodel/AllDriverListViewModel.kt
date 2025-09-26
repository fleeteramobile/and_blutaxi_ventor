package com.blueventor.viewmodel

import ResponseAllDriverList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blueventor.network.NetworkModule_ProvideBaseUrlFactory
import com.blueventor.network.UiState
import com.blueventor.network.request.RequestDriverDetails
import com.blueventor.network.request.RequestGetAllDriverList

import com.blueventor.repository.AllDriverRepository
import com.blueventor.util.logDebugMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Thread.State
import javax.inject.Inject
@HiltViewModel
class AllDriverListViewModel @Inject constructor(val allDriverRepository: AllDriverRepository): ViewModel(){
private val _uiState = MutableStateFlow<UiState<ResponseAllDriverList>>(UiState.Idle)
val uiState: StateFlow<UiState<ResponseAllDriverList>> = _uiState

    fun getDriverDetails(requestGetAllDriverList: RequestGetAllDriverList) {
        logDebugMessage("state_result","1")

        viewModelScope.launch {
            _uiState.value = UiState.Loading
            _uiState.value = allDriverRepository.getAllDriverList(requestGetAllDriverList)
        }


    }
}