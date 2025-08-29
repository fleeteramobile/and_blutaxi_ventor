package com.blueventor.network

import com.blueventor.BuildConfig
import com.blueventor.session.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // FIX 1: Provide the base URL directly from BuildConfig. This is the correct
    // and automated way to handle different environments.
    @Provides
    fun provideBaseUrl(): String {
        return BaseUrlProvider.url
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(sessionManager: SessionManager): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

        // Interceptor to add headers (e.g., the saved authkey)
        val headerInterceptor = Interceptor { chain ->
            val requestBuilder = chain.request().newBuilder()
            val authKey = sessionManager.getString("authkey", "")
            val userAuth = sessionManager.getString("userAuth", "")

            if (authKey.isNotEmpty()) {
                requestBuilder.addHeader("authkey", authKey)
            }
            if (userAuth.isNotEmpty()) {
                requestBuilder.addHeader("userAuth", userAuth)
            }
            chain.proceed(requestBuilder.build())
        }

        // NEW: Interceptor to read and save the authkey from the response
        val authInterceptor = AuthInterceptor(sessionManager)

        return OkHttpClient.Builder()
            .addInterceptor(headerInterceptor)
            .addInterceptor(authInterceptor)  // Add the new interceptor
            .addInterceptor(logging)          // Keep logging last to see the final request/response
            .build()
    }


    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)
}

