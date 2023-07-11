package com.udacity.asteroidradar.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


/**
 * Below are the domain objects for the app.
 */

@Parcelize
data class Asteroid(
    val id: Long,
    val codename: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyDangerous: Boolean
): Parcelable

data class PictureOfDay(
    val mediaType: String,
    val title: String,
    val url: String,
)
