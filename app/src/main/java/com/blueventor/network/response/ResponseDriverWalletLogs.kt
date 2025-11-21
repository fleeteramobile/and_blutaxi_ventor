package com.blueventor.network.response

/**
{
  "message": "Wallet Logs fetched successfully",
  "status": 1,
  "data": {
    "records": [
      {
        "_id": 255,
        "walletlog_id": 255,
        "driver_id": 5,
        "name": "BALAMURUGAN",
        "lastname": "R",
        "mobile_number": "8608332271",
        "country_code": "+91",
        "user_type": "A",
        "amount": 1000,
        "account_balance": 23846.21,
        "createdate": "2025-11-20 11:40:40",
        "recharge_by": "Admin"
      },
      {
        "_id": 254,
        "walletlog_id": 254,
        "driver_id": 5,
        "name": "BALAMURUGAN",
        "lastname": "R",
        "mobile_number": "8608332271",
        "country_code": "+91",
        "user_type": "M",
        "amount": 5000,
        "account_balance": 23846.21,
        "createdate": "2025-11-20 11:22:34"
      },
      {
        "_id": 253,
        "walletlog_id": 253,
        "driver_id": 5,
        "name": "BALAMURUGAN",
        "lastname": "R",
        "mobile_number": "8608332271",
        "country_code": "+91",
        "user_type": "M",
        "amount": 5000,
        "account_balance": 23846.21,
        "createdate": "2025-11-20 11:18:26"
      },
      {
        "_id": 252,
        "walletlog_id": 252,
        "driver_id": 5,
        "name": "BALAMURUGAN",
        "lastname": "R",
        "mobile_number": "8608332271",
        "country_code": "+91",
        "user_type": "D",
        "amount": 5000,
        "account_balance": 23846.21,
        "createdate": "2025-11-20 10:33:35",
        "recharge_by": "Driver"
      }
    ],
    "totals": {
      "total_fare": " 16000.00",
      "total_driver_fare": " 5000.00",
      "total_admin_fare": " 1000.00"
    }
  }
}
*/
data class ResponseDriverWalletLogs(
    val `data`: Data,
    val message: String,
    val status: Int
) {
    data class Data(
        val records: List<Record>,
        val totals: Totals
    ) {
        data class Record(
            val _id: String,
            val account_balance: Double,
            val amount: String,
            val country_code: String,
            val createdate: String,
            val driver_id: String,
            val lastname: String,
            val mobile_number: String,
            val name: String,
            val recharge_by: String,
            val user_type: String,
            val walletlog_id: String
        )

        data class Totals(
            val total_admin_fare: String,
            val total_driver_fare: String,
            val total_fare: String
        )
    }
}