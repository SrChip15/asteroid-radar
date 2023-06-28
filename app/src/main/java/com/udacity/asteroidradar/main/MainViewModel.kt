package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.network.Network
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel : ViewModel() {

    private val _picture = MutableLiveData<PictureOfDay>()

    val picture: LiveData<PictureOfDay>
        get() = _picture

    init {
        viewModelScope.launch {
            try {
                _picture.value = Network.apod.getPictureOfTheDay(BuildConfig.API_KEY)
            } catch (e: Exception) {
                e.message?.let { Timber.i(it) }
            }
        }
    }
}