package com.blueventor.network.response

data class ResponseTripList(
    val trip_details: TripDetails
) {
    data class TripDetails(
        val message: String,
        val result: List<Result>,
        val status: Int
    ) {
        data class Result(
            val _id: Int,
            val actual_pickup_time: String,
            val amt: Double,
            val approx_fare: Double,
            val company_id: Int,
            val createdate: String,
            val distance: Double,
            val driver_id: Int,
            val drop_latitude: Double,
            val drop_location: String,
            val drop_longitude: Double,
            val drop_time: String,
            val passengers_id: Int,
            val pickup_latitude: Double,
            val pickup_longitude: Double,
            val pickup_time: String,
            val travel_status: Int,
            val trip_type: String,
            val waiting_fare_mt: Int
        )
    }
}