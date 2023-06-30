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
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class MainViewModel : ViewModel() {

    private val _asteroids = MutableLiveData<List<Asteroid>>()
    private val _picture = MutableLiveData<PictureOfDay>()

    val asteroids: LiveData<List<Asteroid>>
        get() = _asteroids
    val picture: LiveData<PictureOfDay>
        get() = _picture

    init {
        // Get list of asteroids from [AsteroidService]
        AsteroidApi.asteroidService.getFeed(BuildConfig.API_KEY).enqueue(object:
            Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                Timber.i("Success: ${response.isSuccessful}")
                response.body()?.let {
                    try {
                        val parsedJSON = JSONObject(it)
                         _asteroids.value = parseAsteroidsJsonResult(parsedJSON)
                        Timber.i(_asteroids.value?.toString())
                    } catch (e: JSONException) {
                        Timber.e("JSON conversion: ${e.message}")
                    }
                }
                // Timber.i(responseBody)
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Timber.e("Failure: ${t.message}")
            }
        })

        viewModelScope.launch {
            try {
                _picture.value = ApodApi.apodService.getPictureOfTheDay(BuildConfig.API_KEY)
            } catch (e: Exception) {
                e.message?.let { Timber.e(it) }
            }
        }

    }
}