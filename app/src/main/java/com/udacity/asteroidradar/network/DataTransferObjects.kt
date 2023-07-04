package com.udacity.asteroidradar.network

import com.squareup.moshi.Json
import com.udacity.asteroidradar.database.DatabaseAsteroid
import com.udacity.asteroidradar.database.DatabasePictureOfDay
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.util.dateStringAsDate

data class PictureOfDayContainer(
    val copyright: String,
    val date: String,
    @Json(name = "media_type") val mediaType: String,
    val title: String,
    val url: String,
)

fun PictureOfDayContainer.asDomainModel(): PictureOfDay {
    return PictureOfDay(
        mediaType = mediaType,
        title = title,
        url = url
    )
}

fun PictureOfDayContainer.asDatabaseModel(): DatabasePictureOfDay {
    return DatabasePictureOfDay(
        date = date,
        mediaType = mediaType,
        title = title,
        url = url
    )
}

data class NetworkAsteroidContainer(
    @Json(name = "element_count") val elementCount: Int,
    @Json(name = "near_earth_objects") val nearEarthObjects: Map<String, List<NetworkAsteroid>>
)

data class NetworkAsteroid(
    val id: Long,
    @Json(name = "name") val codename: String,
    @Json(name = "close_approach_data") val closeApproachData: List<CloseApproachData>,
    @Json(name = "estimated_diameter") val estimatedDiameterData: EstimatedDiameterData,
    @Json(name = "absolute_magnitude_h") val absoluteMagnitude: Double,
    @Json(name = "is_potentially_hazardous_asteroid") val isPotentiallyHazardous: Boolean,
)

data class EstimatedDiameterData(
    @Json(name = "kilometers") val estimatedDiameterInKilometers: EstimatedDiameterInKilometers
)

data class EstimatedDiameterInKilometers(
    @Json(name = "estimated_diameter_max") val estimatedDiameter: Double
)

data class CloseApproachData(
    @Json(name = "close_approach_date") val closeApproachDate: String,
    @Json(name = "relative_velocity") val relativeVelocityData: RelativeVelocityData,
    @Json(name = "miss_distance") val missDistance: CloseApproachMissDistance,
)

data class RelativeVelocityData(
    @Json(name = "kilometers_per_second") val relativeVelocity: Double
)

data class CloseApproachMissDistance(
    @Json(name = "astronomical") val distanceFromEarth: Double,
)

fun NetworkAsteroidContainer.asDomainModel(): List<Asteroid> {
    return nearEarthObjects.values.flatten().map {
        Asteroid(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachData[0].closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameterData.estimatedDiameterInKilometers.estimatedDiameter,
            relativeVelocity = it.closeApproachData[0].relativeVelocityData.relativeVelocity,
            distanceFromEarth = it.closeApproachData[0].missDistance.distanceFromEarth,
            isPotentiallyDangerous = it.isPotentiallyHazardous,
        )
    }
}

fun NetworkAsteroidContainer.asDatabaseModel(): Array<DatabaseAsteroid> {
    return nearEarthObjects.values.flatten().map {
        DatabaseAsteroid(
            id = it.id,
            codename = it.codename,
            closeApproachDate = dateStringAsDate(it.closeApproachData[0].closeApproachDate),
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameterData.estimatedDiameterInKilometers.estimatedDiameter,
            relativeVelocity = it.closeApproachData[0].relativeVelocityData.relativeVelocity,
            distanceFromEarth = it.closeApproachData[0].missDistance.distanceFromEarth,
            isPotentiallyDangerous = it.isPotentiallyHazardous,
        )
    }.toTypedArray()
}
