package com.blueventor.network.response

data class ResponseDriverWalletRecharge(
    val account_balance: Double,
    val auth_key: String,
    val message: String,
    val status: Int
)