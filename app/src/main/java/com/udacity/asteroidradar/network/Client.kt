package com.udacity.asteroidradar.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.Constants.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


/**
 * Singleton network client that facilitates converter factory
 * specification. This helps with dynamically setting different converters
 * when making network calls to different APIs.
 */
object Client {

    /**
     * Setting expectation that the Retrofit object would be initialized prior
     * to its utilization
     */
    private lateinit var INSTANCE: Retrofit

    /**
     * Provides a singleton instance of the network client
     *
     * @param converterFactory [ConverterFactory] Converter factory for the instance
     *
     * @return [Retrofit] instance
     */
    fun getInstance(converterFactory: Converter.Factory): Retrofit {
        synchronized(this) {
            INSTANCE = if (!::INSTANCE.isInitialized) {
                Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(Logger.getLogger())
                    .addConverterFactory(converterFactory)
                    .build()
            } else {
                INSTANCE.newBuilder().addConverterFactory(converterFactory).build()
            }
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

/**
 * Provides converter factories that could be used with the retrofit client instance
 */
object ConverterFactory {
    /**
     * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
     * full Kotlin compatibility.
     */
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val moshiConverterFactory: MoshiConverterFactory = MoshiConverterFactory.create(moshi)

    /**
     * Build the Scalars object that Retrofit will be using to parse selectively from the
     * NeoWs network response
     */
    val scalarsConverterFactory: ScalarsConverterFactory = ScalarsConverterFactory.create()
}
