package com.example.appointmentcalendar.util

import io.mockk.every
import io.mockk.mockkStatic
import org.junit.Assert
import org.junit.Test
import java.util.*

class TransferDateUtilTest {
    @Test
    fun getStartAtTest() {
        val expectedStartAt = "2022-5-15T07:00:00.000Z"

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

        val testTime = "2022-5-15T07:00:00.000Z"
        val expectedTime = "2022/05/15 00:00"

        Assert.assertEquals(expectedTime, getTimeWithTimeZone(testTime))
    }
}