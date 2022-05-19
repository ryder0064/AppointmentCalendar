package com.example.appointmentcalendar.test_util

import com.example.appointmentcalendar.data.model.TeacherSchedule

val testTeacherSchedule = TeacherSchedule(
    id = 0,
    availableList = listOf(
        TeacherSchedule.ClassTime(
            start = "2022-05-20T16:00:00Z",
            end = "2022-05-20T17:00:00Z"
        ),
        TeacherSchedule.ClassTime(
            start = "2022-05-20T22:00:00Z",
            end = "2022-05-21T00:30:00Z"
        )
    ),
    bookedList = listOf(
        TeacherSchedule.ClassTime(
            start = "2022-05-22T02:00:00Z",
            end = "2022-05-22T02:30:00Z"
        )
    )
)
