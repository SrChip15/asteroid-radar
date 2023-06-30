package com.udacity.asteroidradar.network

import org.json.JSONObject
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * A retrofit service to fetch a list of near earth objects
 * from NeoWs (Near Earth Object Web Service) maintained by
 * the [SpaceRocks Team](https://github.com/SpaceRocks/).
 */
interface AsteroidService {
    @GET("neo/rest/v1/feed")
    suspend fun getFeed(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("api_key") apiKey: String
    ): JSONObject
}

object AsteroidApi {
    val asteroidService: AsteroidService by lazy {
        Client.getInstance(ConverterFactory.scalarsConverterFactory)
            .create(AsteroidService::class.java)
    }
}
