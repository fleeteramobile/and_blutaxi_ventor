package com.blueventor.network

import com.blueventor.session.SessionManager
import com.blueventor.util.logDebugMessage
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.IOException

class AuthInterceptor(private val sessionManager: SessionManager) : Interceptor {
    private val gson = Gson()

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        val responseBody = response.body
        val contentType = responseBody?.contentType()
        val content = responseBody?.string()

        logDebugMessage("AuthInterceptor", "Received JSON content: $content")
        logDebugMessage("AuthInterceptor", "1")
        if (response.isSuccessful && !content.isNullOrEmpty()) {
            logDebugMessage("AuthInterceptor", "3")
            try {
                val authKeyResponse = gson.fromJson(content, AuthKeyResponse::class.java)
                val authKey = authKeyResponse.authKey
                if (!authKey.isNullOrEmpty()) {
                    sessionManager.saveString("authkey", authKey)
                    logDebugMessage("AuthInterceptor", "AuthKey saved: $authKey")
                }
            } catch (e: Exception) {
                logDebugMessage("AuthInterceptor", "Error parsing JSON: ${e.message}")
            }

        }

        // rebuild response with original body
        val newResponseBody = ResponseBody.create(contentType, content ?: "")
        return response.newBuilder().body(newResponseBody).build()
    }
}

