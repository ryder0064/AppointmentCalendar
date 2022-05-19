package com.example.appointmentcalendar

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.appointmentcalendar.data.repository.ScheduleRepository
import com.example.appointmentcalendar.test_util.getValue
import com.example.appointmentcalendar.test_util.testTeacherSchedule
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class TeacherScheduleViewModelTest {
    private val testTeacherScheduleData = testTeacherSchedule

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    @MockK
    private lateinit var repository: ScheduleRepository
    @MockK
    private lateinit var viewModel: TeacherScheduleViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = TeacherScheduleViewModel(repository)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun cleanup() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun getTeacherScheduleTest() {
        val teacherScheduleLiveData = MutableLiveData(testTeacherScheduleData)
        coEvery { repository.getTeacherSchedule() } returns teacherScheduleLiveData

        val result = getValue(
            repository.getTeacherSchedule()
        )

        viewModel.refreshTeacherSchedule()

        Assert.assertEquals(result, testTeacherScheduleData)
    }

    @Test
    fun refreshDataTest() {
        viewModel.refreshTeacherSchedule()

        coVerify(exactly = 1) { repository.refreshData() }
    }

    @Test
    fun cleanSnackBarMessageTest() {
        val originalMessage = "Unexpected problem occurred"
        val snackBarMessageLiveData = MutableLiveData(originalMessage)
        every { repository.getSnackBarMessage() } returns snackBarMessageLiveData
        var result = getValue(
            repository.getSnackBarMessage()
        )

        Assert.assertEquals(result, originalMessage)
        val expectedMessage = ""

        viewModel.clearSnackBar()
        result = getValue(
            repository.getSnackBarMessage()
        )

        Assert.assertEquals(expectedMessage, result)
    }
}