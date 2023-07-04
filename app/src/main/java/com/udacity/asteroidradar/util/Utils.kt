package com.udacity.asteroidradar.util

import java.text.SimpleDateFormat
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