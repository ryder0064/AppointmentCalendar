package com.example.appointmentcalendar.data.remote

import com.example.appointmentcalendar.data.model.TeacherSchedule
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ScheduleService {
    @GET("{teacherName}/schedule")
    suspend fun getTeacherSchedule(
        @Path("teacherName") teacherName: String,
        @Query("started_at") startedAt: String
    ): Response<TeacherSchedule>
}