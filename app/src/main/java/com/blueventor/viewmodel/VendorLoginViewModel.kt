package com.blueventor.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blueventor.network.UiState
import com.blueventor.network.request.Requestloginaccess
import com.blueventor.network.response.RespondeLoginAccess
import com.blueventor.network.response.ResponseCheckCompanyDomain
import com.blueventor.repository.VendorLoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VendorLoginViewModel @Inject constructor(val vendorLoginRepository: VendorLoginRepository) :
    ViewModel() {
    private val _uiState = MutableStateFlow<UiState<RespondeLoginAccess>>(UiState.Idle)
    val uiState: StateFlow<UiState<RespondeLoginAccess>> = _uiState
    fun getVendorLoginAccess(requestloginaccess: Requestloginaccess) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            _uiState.value = vendorLoginRepository.loginAccess(requestloginaccess)
        }


    }


}