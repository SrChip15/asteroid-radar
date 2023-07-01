package com.udacity.asteroidradar.network

import com.squareup.moshi.Json
import com.udacity.asteroidradar.Asteroid

data class NeoWsResponse(
    @Json(name = "element_count") val elementCount: Int,
    @Json(name = "near_earth_objects") val nearEarthObjects: Map<String, List<Asteroid>>
)
