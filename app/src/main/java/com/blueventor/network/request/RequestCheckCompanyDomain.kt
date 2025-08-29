package com.blueventor.network.request

data class RequestCheckCompanyDomain(
    val company_domain: String,
    val company_main_domain: String,
    val device_type: String = "1"
)
