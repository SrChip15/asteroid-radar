package com.udacity.asteroidradar.database

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase

@Dao
interface AsteroidDao {

    @Query("SELECT * FROM DatabaseAsteroid ORDER BY closeApproachDate ASC")
    fun getAsteroids(): List<DatabaseAsteroid>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: DatabaseAsteroid)

    @Query("DELETE FROM DatabaseAsteroid WHERE closeApproachDate <= :targetDate")
    fun deleteByDate(targetDate: Long): Int

    @Query("SELECT * FROM DatabaseAsteroid WHERE closeApproachDate >= :start AND closeApproachDate < :end ORDER BY closeApproachDate ASC")
    fun getAsteroidsByDate(start: Long, end: Long): List<DatabaseAsteroid>?
}

@Database(entities = [DatabaseAsteroid::class], version = 1)
abstract class AsteroidDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
}

private lateinit var INSTANCE: AsteroidDatabase

fun getDatabase(context: Context): AsteroidDatabase {
    synchronized(AsteroidDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room
                .databaseBuilder(context, AsteroidDatabase::class.java, "asteroids")
                .build()
        }
    }

    return INSTANCE
}
