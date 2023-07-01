package com.udacity.asteroidradar.network

import com.udacity.asteroidradar.PictureOfDay
import retrofit2.http.GET

interface NasaService {

    @GET("planetary/apod")
    suspend fun getPictureOfTheDay(): PictureOfDay

    @GET("neo/rest/v1/feed")
    suspend fun getFeed(): NeoWsResponse
}

object NasaApi {
    val nasaService: NasaService by lazy {
        Client.getInstance().create(NasaService::class.java)
    }
}