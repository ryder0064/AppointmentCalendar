package com.example.appointmentcalendar.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.appointmentcalendar.data.local.dao.TeacherScheduleDao
import com.example.appointmentcalendar.data.model.TeacherSchedule
import com.example.appointmentcalendar.data.remote.ScheduleService
import com.example.appointmentcalendar.test_util.getValue
import com.example.appointmentcalendar.test_util.testTeacherSchedule
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Response

@RunWith(JUnit4::class)
class ScheduleRepositoryTest{
    private val testTeacherScheduleData = testTeacherSchedule
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var scheduleService:ScheduleService
    @MockK
    private lateinit var scheduleDao: TeacherScheduleDao

    private lateinit var repository: ScheduleRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        repository = ScheduleRepository(scheduleService, scheduleDao)
    }

    @Test
    fun getTeacherScheduleTest() {
        val teacherScheduleLiveData = MutableLiveData(testTeacherScheduleData)
        every { scheduleDao.getTeacherSchedule() } returns teacherScheduleLiveData

        val result = getValue(
            repository.getTeacherSchedule()
        )

        Assert.assertEquals(result, testTeacherScheduleData)
    }

    @Test
    fun refreshDataSuccessVerifyDataInsertedOnce() = runBlocking {
        val responseSuccess: Response<TeacherSchedule> = Response.success(testTeacherScheduleData)
        coEvery { scheduleService.getTeacherSchedule(any(), any()) } returns responseSuccess

        repository.refreshData()

        coVerify(exactly = 1) { scheduleService.getTeacherSchedule(any(), any()) }
        verify(exactly = 1) { scheduleDao.insertTeacherSchedule(testTeacherScheduleData) }
    }

    @Test
    fun refreshDataSuccessButNullVerifyDeletedOnce() = runBlocking {
        val responseSuccess: Response<TeacherSchedule> = Response.success(null)
        coEvery { scheduleService.getTeacherSchedule(any(), any()) } returns responseSuccess

        repository.refreshData()

        coVerify(exactly = 1) { scheduleService.getTeacherSchedule(any(), any()) }
        verify(exactly = 1) { scheduleDao.deleteTeacherSchedule() }
    }

    @Test
    fun refreshDataErrorVerifyMessage() = runBlocking {
        val expectedSnackMessage = "Unexpected problem occurred"
        val responseError: Response<TeacherSchedule> = Response.error(
            403,
            ResponseBody.create(
                MediaType.parse("application/json"),
                "Bad Request"
            )
        )
        coEvery { scheduleService.getTeacherSchedule(any(), any()) } returns responseError

        repository.refreshData()
        val snackMessageResult = getValue(repository.getSnackBarMessage())

        coVerify(exactly = 1) { scheduleService.getTeacherSchedule(any(), any()) }
        verify(exactly = 0) { scheduleDao.insertTeacherSchedule(any()) }
        Assert.assertEquals(expectedSnackMessage, snackMessageResult)
    }
}