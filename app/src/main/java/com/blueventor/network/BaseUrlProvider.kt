package com.blueventor.network

import com.blueventor.BuildConfig


object BaseUrlProvider {
    val url: String
        get() = if (BuildConfig.DEBUG) {
            when (BuildConfig.FLAVOR) {
                "dev", "uat", "testing" -> BuildConfig.BASE_URL
                else -> throw IllegalStateException("Unknown debug flavor")
            }
        } else {
            // Release / production always
            BuildConfig.BASE_URL
        }
}
