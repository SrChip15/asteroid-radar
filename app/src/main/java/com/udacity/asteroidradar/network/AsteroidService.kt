package com.udacity.asteroidradar.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * A retrofit service to fetch a list of near earth objects
 * from NeoWs (Near Earth Object Web Service) maintained by
 * the [SpaceRocks Team](https://github.com/SpaceRocks/).
 */
interface AsteroidService {
    @GET("neo/rest/v1/feed")
    fun getFeed(
        @Query("api_key") apiKey: String
    ): Call<String>
}

object AsteroidApi {
    val asteroidService: AsteroidService by lazy {
        Client.getInstance(ConverterFactory.scalarsConverterFactory)
            .create(AsteroidService::class.java)
    }
}
