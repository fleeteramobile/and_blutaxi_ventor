package com.blueventor.network.response

data class ResponseCheckCompanyDomain(
    val androidPaths: AndroidPaths,
    val apikey: String,
    val auth_key: String,
    val baseurl: String,
    val baseurl_ms: String,
    val default_language: String,
    val encode: String,
    val https_base_url: String,
    val message: String,
    val status: Int
) {
    data class AndroidPaths(
        val driverColorCode: String,
        val driver_language: List<Any>,
        val headerLogo_driver: String,
        val headerLogo_passenger: String,
        val signInLogo_driver: String,
        val signInLogo_passenger: String,
        val static_image: String
    )
}