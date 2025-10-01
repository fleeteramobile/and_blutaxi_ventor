package com.blueventor.network.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blueventor.network.UiState
import com.blueventor.network.request.RequestDriverDetails
import com.blueventor.network.request.RequestTripDetails
import com.blueventor.network.request.RequestTripList
import com.blueventor.network.request.Requestloginaccess
import com.blueventor.network.response.RespondeLoginAccess
import com.blueventor.network.response.ResponseCheckCompanyDomain
import com.blueventor.network.response.ResponseDriverDetails
import com.blueventor.network.response.ResponseTripDetails
import com.blueventor.network.response.ResponseTripList
import com.blueventor.network.repository.DriverDetailsRepository
import com.blueventor.network.repository.TripDetailsRepository
import com.blueventor.network.repository.TripListRepository
import com.blueventor.network.repository.VendorLoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TripDetailsViewModel @Inject constructor(val tripDetailsRepository: TripDetailsRepository) :
    ViewModel() {
    private val _uiState = MutableStateFlow<UiState<ResponseTripDetails>>(UiState.Idle)
    val uiState: StateFlow<UiState<ResponseTripDetails>> = _uiState
    fun getDriverDetails(requestTripDetails: RequestTripDetails) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            _uiState.value = tripDetailsRepository.getDriverDetails(requestTripDetails)
        }


    }


}