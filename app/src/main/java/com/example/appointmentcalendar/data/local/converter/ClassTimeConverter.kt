package com.example.appointmentcalendar.data.local.converter

import androidx.room.TypeConverter
import com.example.appointmentcalendar.data.model.TeacherSchedule
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ClassTimeConverter {

    private val gson = Gson()

    @TypeConverter
    fun classTimeListToString(list: List<TeacherSchedule.ClassTime>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun stringToClassTimeList(data: String): List<TeacherSchedule.ClassTime> {
        val listType = object : TypeToken<List<TeacherSchedule.ClassTime>>() {}.type
        return gson.fromJson(data, listType)
    }
}
