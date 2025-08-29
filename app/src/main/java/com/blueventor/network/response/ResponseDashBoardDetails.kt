package com.blueventor.network.response

data class ResponseDashBoardDetails(
    val count: Count,
    val message: String,
    val status: Int
) {
    data class Count(
        val active_driver: Int,
        val approved_driver: Int,
        val blocked_driver: Int,
        val cancel_trips_count: Int,
        val card_payment: Int,
        val cash_payment: Double,
        val completed_trips_count: Int,
        val driver_count: Int,
        val inactive_driver: Int,
        val new_drivers: Int,
        val total_amount: Double,
        val total_trips_count: Int
    )
}