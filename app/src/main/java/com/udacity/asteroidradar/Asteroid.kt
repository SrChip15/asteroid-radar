package com.udacity.asteroidradar

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class Asteroid(
    val id: Long,
    @Json(name = "name") val codename: String,
    @Json (name = "close_approach_data") val closeApproachData: List<CloseApproachData>,
    @Json(name = "estimated_diameter") val estimatedDiameterData: EstimatedDiameterData,
    @Json(name = "absolute_magnitude_h") val absoluteMagnitude: Double,
    @Json(name = "is_potentially_hazardous_asteroid") val isPotentiallyHazardous: Boolean,
) : Parcelable

@Parcelize
data class EstimatedDiameterData(
    @Json(name = "kilometers") val estimatedDiameterInKilometers: EstimatedDiameterInKilometers
): Parcelable

@Parcelize
data class EstimatedDiameterInKilometers(
    @Json(name = "estimated_diameter_max") val estimatedDiameter: Double
): Parcelable

@Parcelize
data class CloseApproachData(
    @Json(name = "close_approach_date") val closeApproachDate: String,
    @Json(name = "relative_velocity") val relativeVelocityData: RelativeVelocityData,
    @Json(name = "miss_distance") val missDistance: CloseApproachMissDistance,
): Parcelable

@Parcelize
data class RelativeVelocityData(
    @Json(name = "kilometers_per_second") val relativeVelocity: Double
): Parcelable

@Parcelize
data class CloseApproachMissDistance(
    @Json(name = "astronomical") val distanceFromEarth: Double,
): Parcelable
