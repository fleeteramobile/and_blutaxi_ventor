package com.blueventor.network.response

data class ResponseGetDriverLocation(
    val `data`: List<Data>,
    val message: String,
    val status: Int
) {
    data class Data(
        val country_code: String,
        val driver_code: String,
        val driver_id: Int,
        val driver_mobile: String,
        val driver_status: String,
        val trip_type: String,
        val loc: Loc,
        val taxi_model: String,
        val taxi_no: String
    ) {
        data class Loc(
            val coordinates: List<Double>,
            val type: String
        )
    }
}