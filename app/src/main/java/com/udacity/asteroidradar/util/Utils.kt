package com.udacity.asteroidradar.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun dateStringAsDate(dateString: String): Long {
    val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
    return dateFormat.parse(dateString)!!.time
}

fun dateAsDateString(timeInMillis: Long): String {
    val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
    return dateFormat.format(Date(timeInMillis))
}

fun getToday(): String {
    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
    val currentTime = calendar.time

    return dateFormat.format(currentTime)
}

fun getTomorrow(): String {
    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())

    calendar.add(Calendar.DAY_OF_YEAR, 1)
    val tomorrowTime = calendar.time

    return dateFormat.format(tomorrowTime)
}
