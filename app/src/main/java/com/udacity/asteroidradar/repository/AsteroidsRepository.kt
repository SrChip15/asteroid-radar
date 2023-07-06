package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.network.NasaApi
import com.udacity.asteroidradar.network.asDatabaseModel
import com.udacity.asteroidradar.util.dateStringAsDate
import com.udacity.asteroidradar.util.getToday
import com.udacity.asteroidradar.util.getYesterday
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AsteroidsRepository(private val database: AsteroidDatabase) {

    val asteroids: LiveData<List<Asteroid>> =
        database.asteroidDao.getAsteroids().map { databaseAsteroid ->
            databaseAsteroid.asDomainModel()
        }

    suspend fun refreshAsteroids(startDate: String = getToday()) {
        withContext(Dispatchers.IO) {
            val asteroids = NasaApi.nasaService.getFeed(startDate)
            database.asteroidDao.insertAll(*asteroids.asDatabaseModel())
        }
    }

    suspend fun deleteOldAsteroids(targetDate: String = getYesterday()) {
        withContext(Dispatchers.IO) {
            database.asteroidDao.deleteByDate(dateStringAsDate(targetDate))
        }
    }
}
