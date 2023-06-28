package com.udacity.asteroidradar.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants.BASE_URL
import com.udacity.asteroidradar.PictureOfDay
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * A retrofit service to fetch the image of the day from the
 * Astronomy Picture of the Day NASA API service
 */
interface ApodService {
    @GET("planetary/apod")
    suspend fun getPictureOfTheDay(@Query("api_key") apiKey: String): PictureOfDay
}

/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

/**
 * Main entry point for network access. Call like `Network.apod.getPictureOfTheDay()`
 */
object Network {
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val apod: ApodService = retrofit.create(ApodService::class.java)
}
