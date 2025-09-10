package com.blueventor.network.request


data class RequestDashBoardCarPerformanceDetails(
    val company_id: String,
    val start_date: String,
    val end_date: String,
    val limit: String,
    val offset: String,
    val search: String,
)
