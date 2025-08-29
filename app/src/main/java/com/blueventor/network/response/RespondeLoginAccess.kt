package com.blueventor.network.response

data class RespondeLoginAccess(
    val detail: Detail,
    val message: String,
    val status: Int,
    val user_key: String
) {
    data class Detail(
        val company_id: Int,
        val company_info: CompanyInfo,
        val country_code: Any,
        val email: String,
        val id: Int,
        val login_city: Int,
        val login_country: Int,
        val login_state: Int,
        val name: String,
        val phone: String
    ) {
        data class CompanyInfo(
            val cancellation_fare: String,
            val company_address: String,
            val company_brand_type: String,
            val company_id: Int,
            val company_name: String,
            val company_status: String,
            val company_tax: String,
            val default_unit: Int,
            val fare_calculation_type: String,
            val userid: Int
        )
    }
}