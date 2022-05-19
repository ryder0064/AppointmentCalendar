package com.example.appointmentcalendar.util

import com.example.appointmentcalendar.data.model.ScheduleItem
import com.example.appointmentcalendar.test_util.testTeacherSchedule
import io.mockk.every
import io.mockk.mockkStatic
import org.junit.Assert
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class TransferDateUtilTest {
    @Test
    fun getStartAtTest() {
        val expectedStartAt = "2022-05-15T07:00:00Z"

        mockkStatic(TimeZone::class)
        every { TimeZone.getDefault() } returns TimeZone.getTimeZone("America/Los_Angeles")

        val testDate = Calendar.getInstance()
        testDate[2022, Calendar.MAY] = 19
        mockkStatic(Calendar::class)
        every { Calendar.getInstance() } returns testDate

        Assert.assertEquals(expectedStartAt, getStartAt())
    }

    @Test
    fun getTimeWithTimeZoneTest() {
        mockkStatic(TimeZone::class)
        every { TimeZone.getDefault() } returns TimeZone.getTimeZone("America/Los_Angeles")

        val testTime = "2022-05-15T07:00:00Z"
        val expectedTime = "2022/05/15 00:00"

        Assert.assertEquals(expectedTime, getFormatTimeFromTimeUTC(testTime, SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault())))
    }

    @Test
    fun getNextStartAtTest() {
        val originalStartAt = "2022-05-15T07:00:00Z"
        val expectedStartAt = "2022-05-22T07:00:00Z"
        Assert.assertEquals(expectedStartAt, getNextStartAt(originalStartAt))
    }

    @Test
    fun getLastStartAtTest() {
        val originalStartAt = "2022-05-22T07:00:00Z"
        val expectedStartAt = "2022-05-15T07:00:00Z"
        Assert.assertEquals(expectedStartAt, getLastStartAt(originalStartAt))
    }

    @Test
    fun getWeekIntervalMessageTest() {
        val originalStartAt = "2022-05-15T07:00:00Z"
        val expectedStartAt = "2022-05-15 - 05-21"
        Assert.assertEquals(expectedStartAt, getWeekIntervalMessage(originalStartAt))
    }

    @Test
    fun getFormatTimeFromTimeLongTest() {
        mockkStatic(TimeZone::class)
        every { TimeZone.getDefault() } returns TimeZone.getTimeZone("Asia/Taipei")

        Assert.assertEquals(
            "週一, 5月 23",
            getFormatTimeFromTimeLong(
                1653235200000,
                SimpleDateFormat("EEE, MMM dd", Locale.getDefault())
            )
        )
    }

    @Test
    fun getTeacherScheduleInfoTest() {
        val fakeStartAt = "2022-05-21T16:00:00Z"
        val testTeacherSchedule = testTeacherSchedule
        val teacherScheduleInfo = getTeacherScheduleInfo(fakeStartAt, testTeacherSchedule)
        val expectedLong = 1653580800000 //週五, 5月 27
        Assert.assertNotNull(teacherScheduleInfo[expectedLong])
        Assert.assertEquals(
            ScheduleItem(time = "16:00", isAvailable = true),
            teacherScheduleInfo[expectedLong]!!.first()
        )
    }
}