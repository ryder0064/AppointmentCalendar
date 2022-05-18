package com.example.appointmentcalendar.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.appointmentcalendar.data.model.TeacherSchedule

@Dao
interface TeacherScheduleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTeacherSchedule(schedule: TeacherSchedule)

    @Query("SELECT * FROM teacher_schedule_table")
    fun getTeacherSchedule(): LiveData<TeacherSchedule>

    @Query("DELETE FROM teacher_schedule_table")
    fun deleteTeacherSchedule()
}
