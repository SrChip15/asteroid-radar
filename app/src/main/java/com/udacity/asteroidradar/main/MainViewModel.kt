package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.network.ApodApi
import com.udacity.asteroidradar.network.AsteroidApi
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
                _picture.value = ApodApi.apodService.getPictureOfTheDay(BuildConfig.API_KEY)
                val rawJson = AsteroidApi.asteroidService.getFeed("2023-06-22", "2023-06-29", BuildConfig.API_KEY)
                _asteroids.value = parseAsteroidsJsonResult(rawJson)
                Timber.i("first asteroid: ${_asteroids.value?.first()?.codename}")
            } catch (e: Exception) {
                e.message?.let { Timber.e(it) }
            }
        }
    }
}