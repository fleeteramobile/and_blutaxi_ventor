package com.blueventor.network.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blueventor.network.UiState
import com.blueventor.network.request.RequestCheckCompanyDomain
import com.blueventor.network.response.ResponseCheckCompanyDomain
import com.blueventor.network.repository.CheckCompanyDomainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckCompanyDomainViewModel @Inject constructor(val checkCompanyDomainRepository: CheckCompanyDomainRepository): ViewModel() {
    private val _uiState = MutableStateFlow<UiState<ResponseCheckCompanyDomain>>(UiState.Idle)
    val uiState: StateFlow<UiState<ResponseCheckCompanyDomain>> = _uiState
    fun checkCompanyDomain(request: RequestCheckCompanyDomain) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            _uiState.value = checkCompanyDomainRepository.checkCompany(request)
        }
    }


}