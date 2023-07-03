package com.udacity.asteroidradar.network

import com.udacity.asteroidradar.PictureOfDay
import retrofit2.http.GET

interface NasaService {

    /**
     * Retrofit service to fetch astronomy picture of the day ('apod')
     * from the apod service, which is used in the main view UI
     */
    @GET("planetary/apod")
    suspend fun getPictureOfTheDay(): PictureOfDay

    /**
     * Retrofit service to fetch a list of asteroids from the NeoWs service
     * and this is used to display the list of asteroids in the recycler view
     * in the main view UI
     */
    @GET("neo/rest/v1/feed")
    suspend fun getFeed(): NeoWsResponse
}

/**
 * Provides Singleton object access to all callers requiring network service.
 */
object NasaApi {
    val nasaService: NasaService by lazy {
        Client.getInstance().create(NasaService::class.java)
    }
}