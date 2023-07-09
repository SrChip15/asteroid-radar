package com.udacity.asteroidradar.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.network.NasaApi
import com.udacity.asteroidradar.network.asDomainModel
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _picture = MutableLiveData<PictureOfDay>()
    private val _navigatingToDetailView = MutableLiveData<Asteroid?>()
    private val _asteroids = MutableLiveData<List<Asteroid>>()

    private val database = getDatabase(application)
    private val asteroidsRepository = AsteroidsRepository(database)

    val asteroids: LiveData<List<Asteroid>>
        get() = _asteroids

    val navigatingToDetailView: LiveData<Asteroid?>
        get() = _navigatingToDetailView

    val picture: LiveData<PictureOfDay>
        get() = _picture

    init {
        viewModelScope.launch {
            try {
                // TODO - Handle network errors
                // Get picture of data from apod endpoint
                val apodResponse = NasaApi.nasaService.getPictureOfTheDay()
                _picture.value = apodResponse.asDomainModel()

                // Get list of asteroids from neows endpoint
                asteroidsRepository.refreshAsteroids()
                _asteroids.value = asteroidsRepository.asteroidsFromDatabase()
            } catch (e: Exception) {
                e.message?.let { Timber.e(it) }
            }
        }
    }

    fun fetchAsteroids() {
        viewModelScope.launch {
            _asteroids.value = asteroidsRepository.asteroidsFromDatabase()
        }
    }

    fun fetchAsteroidsForToday() {
        viewModelScope.launch {
            getAsteroidsForToday()
        }
    }

    private suspend fun getAsteroidsForToday() {
        _asteroids.value = asteroidsRepository.getAsteroidsForToday()
    }

    fun onAsteroidClicked(asteroid: Asteroid) {
        _navigatingToDetailView.value = asteroid
    }

    fun doneNavigatingToDetailView() {
        _navigatingToDetailView.value = null
    }


    /**
     * Factory for constructing MainViewModel with parameter
     */
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}