package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.network.NasaApi
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel : ViewModel() {

    private val _asteroids = MutableLiveData<List<Asteroid>>()
    private val _picture = MutableLiveData<PictureOfDay>()

    val asteroids: LiveData<List<Asteroid>>
        get() = _asteroids
    val picture: LiveData<PictureOfDay>
        get() = _picture

    init {

        viewModelScope.launch {
            try {
                // Get picture of data from apod endpoint
                _picture.value = NasaApi.nasaService.getPictureOfTheDay()

                // Get list of asteroids from neows endpoint
                val neoWsResponse = NasaApi.nasaService.getFeed()
                _asteroids.value = neoWsResponse.nearEarthObjects.values.flatten()
            } catch (e: Exception) {
                e.message?.let { Timber.e(it) }
            }
        }

    }
}