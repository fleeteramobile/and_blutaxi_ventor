package com.blueventor.network.response

data class ResponseDashBoardDetails(
    val count: Count,
    val message: String,
    val status: Int
) {
    data class Count(
        val active_driver: String,
        val approved_driver: String,
        val blocked_driver: String,
        val cancel_trips_count: String,
        val card_payment: String,
        val cash_payment: Double,
        val completed_trips_count: String,
        val driver_count: String,
        val inactive_driver: String,
        val new_drivers: String,
        val total_amount: Double,
        val total_trips_count: String
    )
}