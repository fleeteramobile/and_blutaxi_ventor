package com.blueventor.network.response

data class ResponseCarPerformance(
    val `data`: List<Data>,
    val message: String,
    val status: Int
) {
    data class Data(
        val average_rating: Int,
        val company_name: String,
        val driver_code: String,
        val driver_name: String,
        val model_name: String,
        val taxi_no: String,
        val total_amount: Double,
        val trip_count: Int
    )
}