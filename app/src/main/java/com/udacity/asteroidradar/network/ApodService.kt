package com.udacity.asteroidradar.network

import com.udacity.asteroidradar.PictureOfDay
import retrofit2.http.GET

/**
 * A retrofit service to fetch the image of the day from the
 * Astronomy Picture of the Day NASA API service
 */
interface ApodService {
    @GET("planetary/apod")
    suspend fun getPictureOfTheDay(): PictureOfDay
}

/**
 * Main entry point for network access. Call like `Network.apod.getPictureOfTheDay()`
 */
object ApodApi {
    val apodService: ApodService by lazy {
        Client.getInstance(ConverterFactory.moshiConverterFactory).create(ApodService::class.java)
    }

}
