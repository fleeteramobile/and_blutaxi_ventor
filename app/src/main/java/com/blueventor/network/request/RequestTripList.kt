package com.blueventor.network.request


data class RequestTripList(
    val company_id: String,
    val start_date: String,
    val end_date: String,
    val travel_status: String,
    val limit: String,
    val driver_id: String,
    val offset: String,
    val search: String
)
