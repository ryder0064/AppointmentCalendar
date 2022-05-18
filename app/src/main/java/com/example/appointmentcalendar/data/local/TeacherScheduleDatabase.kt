package com.example.appointmentcalendar.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.appointmentcalendar.data.local.converter.ClassTimeConverter
import com.example.appointmentcalendar.data.local.dao.TeacherScheduleDao
import com.example.appointmentcalendar.data.model.TeacherSchedule

@Database(entities = [TeacherSchedule::class], version = 1)
@TypeConverters(value = [ClassTimeConverter::class])
abstract class TeacherScheduleDatabase : RoomDatabase() {
    abstract fun teacherScheduleDao(): TeacherScheduleDao
}
