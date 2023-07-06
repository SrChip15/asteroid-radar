package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidsRepository
import com.udacity.asteroidradar.util.getTomorrow
import timber.log.Timber

class RefreshDataWork(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }

    override suspend fun doWork(): Result {

        val database = getDatabase(applicationContext)
        val repository = AsteroidsRepository(database)

        return try {
            repository.deleteOldAsteroids()
            repository.refreshAsteroids(getTomorrow())
            Result.success()
        } catch (e: Exception) {
            e.message?.let { Timber.e(it) }
            Result.retry()
        }
    }
}
