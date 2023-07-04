package com.udacity.asteroidradar.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.network.NasaApi
import com.udacity.asteroidradar.network.asDomainModel
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _asteroids = MutableLiveData<List<Asteroid>>()
    private val _picture = MutableLiveData<PictureOfDay>()
    private val _navigatingToDetailView = MutableLiveData<Asteroid?>()

    val navigatingToDetailView: LiveData<Asteroid?>
        get() = _navigatingToDetailView

    val asteroids: LiveData<List<Asteroid>>
        get() = _asteroids

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
                val neoWsResponse = NasaApi.nasaService.getFeed()
                _asteroids.value = neoWsResponse.asDomainModel()
            } catch (e: Exception) {
                e.message?.let { Timber.e(it) }
            }
        }
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