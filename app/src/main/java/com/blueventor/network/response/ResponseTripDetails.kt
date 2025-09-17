package com.blueventor.network.response

data class ResponseTripDetails(
    val message: String,
    val status: Int,
    val trip_details: TripDetails
) {
    data class TripDetails(
        val _id: Int,
        val actual_pickup_time: String,
        val amt: Double,
        val approx_fare: Double,
        val createdate: String,
        val distance: Double,
        val driver_country_code: String,
        val driver_id: Int,
        val driver_lastname: String,
        val driver_name: String,
        val driver_phone: String,
        val drop_latitude: Double,
        val drop_location: String,
        val drop_longitude: Double,
        val drop_time: String,
        val passenger_country_code: String,
        val passenger_email: String,
        val passenger_name: String,
        val passenger_phone: String,
        val passengers_id: Int,
        val pickup_latitude: Double,
        val pickup_location: String,
        val pickup_longitude: Double,
        val pickup_time: String,
        val trip_type: String,
        val waiting_fare_mt: Int
    )
}