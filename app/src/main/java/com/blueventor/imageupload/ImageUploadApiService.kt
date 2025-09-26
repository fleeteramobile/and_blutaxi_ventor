package com.bluetaxi.driver.imageupload

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ImageUploadApiService {
    @Multipart
    @POST("?type=driver_selfie")
    fun uploadImage(
        @Part image: MultipartBody.Part,
        @Part("driver_id") userId: RequestBody,
        @Query("lang") lang: String,
        @Query("dt") dt: String,
        @Query("i") i: String,
        @Query("pv") pv: String,
        @Query("k") k: String

    ): Call<UploadResponse>

}