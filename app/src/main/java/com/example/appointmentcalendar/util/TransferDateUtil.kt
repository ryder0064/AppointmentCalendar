package com.example.appointmentcalendar.util

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

fun getStartAt(timeZone: TimeZone = TimeZone.getDefault()): String {
    val calendar = Calendar.getInstance(timeZone)
    calendar[Calendar.DAY_OF_WEEK] = calendar.firstDayOfWeek
    calendar[Calendar.HOUR_OF_DAY] = 0
    calendar[Calendar.MINUTE] = 0
    calendar[Calendar.SECOND] = 0
    calendar[Calendar.MILLISECOND] = 0
    val dateFormat: DateFormat =
        SimpleDateFormat("yyyy-M-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    dateFormat.timeZone = TimeZone.getTimeZone("UTC")
    return dateFormat.format(calendar.time)
}

fun getTimeWithTimeZone(time: String, timeZone: TimeZone = TimeZone.getDefault()): String {
    var dateFormat: DateFormat =
        SimpleDateFormat("yyyy-M-dd'T'hh:mm:ss.SSS'Z'", Locale.getDefault())
    dateFormat.timeZone = TimeZone.getTimeZone("UTC")
    val date = dateFormat.parse(time)!!
    dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault())
    dateFormat.setTimeZone(timeZone)
    return dateFormat.format(date)
}