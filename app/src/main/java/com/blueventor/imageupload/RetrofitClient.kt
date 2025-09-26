package com.bluetaxi.driver.imageupload

import android.content.Context
import com.blueventor.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    // ✅ Correct dynamic BASE_URL resolution
    private val BASE_URL: String
        get() = if (BuildConfig.DEBUG) {
            when (BuildConfig.FLAVOR) {
                "dev", "uat", "testing" -> BuildConfig.BASE_URL
                else -> throw IllegalStateException("Unknown debug flavor: ${BuildConfig.FLAVOR}")
            }
        } else {
            BuildConfig.BASE_URL // always production
        }

    // Logging interceptor
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
        else HttpLoggingInterceptor.Level.NONE
    }

    // Header interceptor (injects tokens from SessionSave / SharedPrefs)
    private val headerInterceptor = Interceptor { chain ->
        val requestBuilder = chain.request().newBuilder()
        val authKey = ""
        val userAuth = ""

        if (!authKey.isNullOrEmpty()) {
            requestBuilder.addHeader("authkey", authKey)
        }
        if (!userAuth.isNullOrEmpty()) {
            requestBuilder.addHeader("userAuth", userAuth)
        }

        chain.proceed(requestBuilder.build())
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(headerInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ImageUploadApiService by lazy {
        retrofit.create(ImageUploadApiService::class.java)
    }
}
