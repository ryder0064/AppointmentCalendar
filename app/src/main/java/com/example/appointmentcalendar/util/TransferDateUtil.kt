package com.example.appointmentcalendar.util

import com.example.appointmentcalendar.data.model.ScheduleItem
import com.example.appointmentcalendar.data.model.TeacherSchedule
import java.text.SimpleDateFormat
import java.util.*

fun getStartAt(timeZone: TimeZone = TimeZone.getDefault()): String {
    val calendar = Calendar.getInstance(timeZone)
    calendar[Calendar.DAY_OF_WEEK] = calendar.firstDayOfWeek
    calendar[Calendar.HOUR_OF_DAY] = 0
    calendar[Calendar.MINUTE] = 0
    calendar[Calendar.SECOND] = 0
    calendar[Calendar.MILLISECOND] = 0
    return getTimeUTCFromDate(calendar.time)
}

fun getNextStartAt(startAt: String): String {
    val date = getDateFromTimeUTC(startAt)
    val calendar = Calendar.getInstance()
    calendar.time = date
    calendar[Calendar.WEEK_OF_MONTH]++
    return getTimeUTCFromDate(calendar.time)
}

fun getLastStartAt(startAt: String): String {
    val date = getDateFromTimeUTC(startAt)
    val calendar = Calendar.getInstance()
    calendar.time = date
    calendar[Calendar.WEEK_OF_MONTH]--
    return getTimeUTCFromDate(calendar.time)
}

fun getWeekIntervalMessage(startAt: String): String {
    var message: String
    val date = getDateFromTimeUTC(startAt)
    val calendar = Calendar.getInstance()
    calendar.time = date
    var dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    message = dateFormat.format(calendar.time)
    calendar[Calendar.WEEK_OF_MONTH]++
    calendar[Calendar.DAY_OF_MONTH]--
    dateFormat = SimpleDateFormat("MM-dd", Locale.getDefault())
    message += " - ${dateFormat.format(calendar.time)}"
    return message
}

fun getTeacherScheduleInfo(
    startAt: String,
    teacherSchedule: TeacherSchedule
): Map<Long, ArrayList<ScheduleItem>> {
    val result = TreeMap<Long, ArrayList<ScheduleItem>>()

    val date = getDateFromTimeUTC(startAt)
    val calendar = Calendar.getInstance()
    calendar.time = date

    for (i in 0..6) {
        result[calendar.time.time] = ArrayList()
        calendar[Calendar.DAY_OF_MONTH]++
    }
    transferClassTime(result, teacherSchedule.availableList, true)
    transferClassTime(result, teacherSchedule.bookedList, false)

    return result
}

fun getFormatTimeFromTimeLong(time: Long, dateFormat: SimpleDateFormat): String {
    val date = Date()
    date.time = time
    return dateFormat.format(date)
}

fun getFormatTimeFromTimeUTC(
    timeUTC: String,
    dateFormat: SimpleDateFormat,
    timeZone: TimeZone = TimeZone.getDefault()
): String {
    val date = getDateFromTimeUTC(timeUTC)
    dateFormat.timeZone = timeZone
    return dateFormat.format(date)
}

private fun getFormatTimeFromTimeUTC(timeUTC: String, dateFormat: SimpleDateFormat): String {
    val date = Date()
    date.time = getDateFromTimeUTC(timeUTC).time
    return dateFormat.format(date)
}

private fun getDateFromTimeUTC(timeUTC: String): Date {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    dateFormat.timeZone = TimeZone.getTimeZone("UTC")
    return dateFormat.parse(timeUTC)!!
}

private fun getTimeUTCFromDate(date: Date): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    dateFormat.timeZone = TimeZone.getTimeZone("UTC")
    return dateFormat.format(date)
}

private fun getScheduleItemTimeFromTimeUTC(timeUTC: String, isAvailable: Boolean): ScheduleItem {
    return ScheduleItem(
        getFormatTimeFromTimeUTC(timeUTC, SimpleDateFormat("HH:mm", Locale.getDefault())),
        isAvailable
    )
}

// key: classTime start time
private fun getKeyFromTimeUTC(timeUTC: String): Long {
    val calendar = Calendar.getInstance()
    calendar.time = getDateFromTimeUTC(timeUTC)
    calendar[Calendar.HOUR_OF_DAY] = 0
    calendar[Calendar.MINUTE] = 0
    calendar[Calendar.SECOND] = 0
    calendar[Calendar.MILLISECOND] = 0
    return calendar.time.time
}

private fun transferClassTime(
    result: Map<Long, MutableList<ScheduleItem>>,
    classTimeList: List<TeacherSchedule.ClassTime>,
    isAvailable: Boolean
) {
    classTimeList.forEach {
        val startDate = getDateFromTimeUTC(it.start)
        val tempCalendar = Calendar.getInstance()
        tempCalendar.time = startDate

        while (getTimeUTCFromDate(tempCalendar.time) != it.end) {
            val startTimeUTC = getTimeUTCFromDate(tempCalendar.time)
            result[getKeyFromTimeUTC(startTimeUTC)]?.add(
                getScheduleItemTimeFromTimeUTC(startTimeUTC, isAvailable)
            )
            tempCalendar.add(Calendar.MINUTE, 30)
        }
    }
}