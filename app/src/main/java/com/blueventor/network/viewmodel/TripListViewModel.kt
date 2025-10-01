package com.blueventor.network.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blueventor.network.UiState
import com.blueventor.network.request.RequestDriverDetails
import com.blueventor.network.request.RequestTripList
import com.blueventor.network.request.Requestloginaccess
import com.blueventor.network.response.RespondeLoginAccess
import com.blueventor.network.response.ResponseCheckCompanyDomain
import com.blueventor.network.response.ResponseDriverDetails
import com.blueventor.network.response.ResponseTripList
import com.blueventor.network.repository.DriverDetailsRepository
import com.blueventor.network.repository.TripListRepository
import com.blueventor.network.repository.VendorLoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TripListViewModel @Inject constructor(val tripListRepository: TripListRepository) :
    ViewModel() {
    private val _uiState = MutableStateFlow<UiState<ResponseTripList>>(UiState.Idle)
    val uiState: StateFlow<UiState<ResponseTripList>> = _uiState
    fun getDriverDetails(requestTripList: RequestTripList) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            _uiState.value = tripListRepository.getTripList(requestTripList)
        }


    }


}