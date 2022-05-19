package com.example.appointmentcalendar.test_util

import com.example.appointmentcalendar.data.model.TeacherSchedule

val testTeacherSchedule = TeacherSchedule(
    id = 0,
    availableList = listOf(
        TeacherSchedule.ClassTime(
            start = "2022-05-27T08:00:00Z",
            end = "2022-05-27T09:30:00Z"
        ),
        TeacherSchedule.ClassTime(
            start = "2022-05-27T14:00:00Z",
            end = "2022-05-27T17:00:00Z"
        )
    ),
    bookedList = listOf(
        TeacherSchedule.ClassTime(
            start = "2022-05-23T15:30:00Z",
            end = "2022-05-23T16:30:00Z"
        )
    )
)
