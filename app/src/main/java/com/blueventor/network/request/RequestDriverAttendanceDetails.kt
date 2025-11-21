package com.blueventor.network.request

data class RequestDriverAttendanceDetails(
    val company_id: String,
    val driver_id: String,
    val month: String ,
    val year: String
)
