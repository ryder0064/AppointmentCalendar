package com.example.appointmentcalendar.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "teacher_schedule_table")
data class TeacherSchedule(
    @PrimaryKey
    val id: Int = 0,
    @SerializedName("available")
    @ColumnInfo(name = "available")
    val availableList: List<ClassTime>,
    @SerializedName("booked")
    @ColumnInfo(name = "booked")
    val bookedList: List<ClassTime>,
) {
    data class ClassTime(
        @ColumnInfo(name = "start")
        @SerializedName("start")
        val start: String,
        @ColumnInfo(name = "end")
        @SerializedName("end")
        val end: String
    )
}