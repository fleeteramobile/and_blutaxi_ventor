package com.blueventor.network.request

data class Requestloginaccess(    val phone: String,
                                  val password: String,
                                  val country_code: String ,
                                  val deviceid: String,
                                  val devicetoken: String )
