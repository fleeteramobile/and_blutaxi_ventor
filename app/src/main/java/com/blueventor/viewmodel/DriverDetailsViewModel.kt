package com.blueventor.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blueventor.network.UiState
import com.blueventor.network.request.RequestDriverDetails
import com.blueventor.network.request.Requestloginaccess
import com.blueventor.network.response.RespondeLoginAccess
import com.blueventor.network.response.ResponseCheckCompanyDomain
import com.blueventor.network.response.ResponseDriverDetails
import com.blueventor.repository.DriverDetailsRepository
import com.blueventor.repository.VendorLoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DriverDetailsViewModel @Inject constructor(val driverDetailsRepository: DriverDetailsRepository) :
    ViewModel() {
    private val _uiState = MutableStateFlow<UiState<ResponseDriverDetails>>(UiState.Idle)
    val uiState: StateFlow<UiState<ResponseDriverDetails>> = _uiState
    fun getDriverDetails(requestDriverDetails: RequestDriverDetails) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            _uiState.value = driverDetailsRepository.getDriverDetails(requestDriverDetails)
        }


    }


}