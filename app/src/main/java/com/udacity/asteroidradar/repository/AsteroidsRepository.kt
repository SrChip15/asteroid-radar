package com.udacity.asteroidradar.repository

import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.DatabaseAsteroid
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.network.NasaApi
import com.udacity.asteroidradar.network.asDatabaseModel
import com.udacity.asteroidradar.util.dateStringAsDate
import com.udacity.asteroidradar.util.getToday
import com.udacity.asteroidradar.util.getTomorrow
import com.udacity.asteroidradar.util.getYesterday
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AsteroidsRepository(private val database: AsteroidDatabase) {

    suspend fun asteroidsFromDatabase(): List<Asteroid> {
        val asteroids: List<DatabaseAsteroid>
        withContext(Dispatchers.IO) {
            asteroids = database.asteroidDao.getAsteroids()
        }

        return asteroids.asDomainModel()
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

    suspend fun getAsteroidsForToday(): List<Asteroid>? {
        var asteroidsForToday: List<DatabaseAsteroid>?
        val today = dateStringAsDate(getToday())
        val tomorrow = dateStringAsDate(getTomorrow())

        withContext(Dispatchers.IO) {
            asteroidsForToday = database.asteroidDao.getAsteroidsByDate(start = today, end = tomorrow)
        }

        return asteroidsForToday?.asDomainModel()
    }
}
