package com.udacity.asteroidradar.network

import retrofit2.http.GET
import retrofit2.http.Query

interface NasaService {

    /**
     * Retrofit service to fetch astronomy picture of the day ('apod')
     * from the apod service, which is used in the main view UI
     */
    @GET("planetary/apod")
    suspend fun getPictureOfTheDay(): PictureOfDayContainer

    /**
     * Retrofit service to fetch a list of asteroids from the NeoWs service
     * and this is used to display the list of asteroids in the recycler view
     * in the main view UI
     */
    @GET("neo/rest/v1/feed")
    suspend fun getFeed(@Query("start_date") startDate: String): NetworkAsteroidContainer

}

/**
 * Provides Singleton object access to all callers requiring network service.
 */
object NasaApi {
    val nasaService: NasaService by lazy {
        Client.getInstance().create(NasaService::class.java)
    }
}