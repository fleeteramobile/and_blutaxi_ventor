package com.blueventor.network.request

data class RequestDriverRecharge(
    val driver_id: String,
    val transaction_id: String,
    val recharge_amount: String
)
