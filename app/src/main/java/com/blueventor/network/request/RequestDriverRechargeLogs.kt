package com.blueventor.network.request

data class RequestDriverRechargeLogs(
    val driver_id: String,
    val end_date: String,
    val start_date: String,
    val offset: String,
    val limit: String
)
