package com.udacity.asteroidradar.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.Constants.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


/**
 * The network client class provides a singleton instance
 * of retrofit service to make network calls to the API
 */
object Client {

    /**
     * Setting expectation that the Retrofit object would be initialized prior
     * to its utilization
     */
    private lateinit var INSTANCE: Retrofit

    /**
     * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
     * full Kotlin compatibility.
     */
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    /**
     * Provides a singleton instance of the network client
     *
     * @return [Retrofit] instance
     */
    fun getInstance(): Retrofit {
        synchronized(this) {
            INSTANCE = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(Logger.getLogger())
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
        }

        return INSTANCE
    }
}

object Logger {
    fun getLogger(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor { chain ->
            val original = chain.request()
            val originalUrl = original.url

            // add authorization as query parameter
            val url = originalUrl.newBuilder()
                .addQueryParameter("api_key", BuildConfig.API_KEY)
                .build()

            val requestBuilder = original.newBuilder().url(url)

            val request = requestBuilder.build()

            chain.proceed(request)
        }

        return httpClient.build()
    }
}
