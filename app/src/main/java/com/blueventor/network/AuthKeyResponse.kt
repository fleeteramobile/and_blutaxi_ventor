package com.blueventor.network

import com.google.gson.annotations.SerializedName

data class AuthKeyResponse(
    // The @SerializedName annotation maps the JSON key to the Kotlin property.
    // This is a best practice and handles cases where the JSON key name is different
    // from your Kotlin variable name (e.g., snake_case vs camelCase).
    @SerializedName("auth_key")
    val authKey: String? = null,

    // If the API key is inside a nested object, you would need to add that object here
    // For example:
    // @SerializedName("data")
    // val data: DataObject? = null

    // You can add other fields from your JSON response here if needed,
    // like "message" or "status"
)

