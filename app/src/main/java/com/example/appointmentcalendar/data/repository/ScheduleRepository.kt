package com.example.appointmentcalendar.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.appointmentcalendar.data.local.dao.TeacherScheduleDao
import com.example.appointmentcalendar.data.model.TeacherSchedule
import com.example.appointmentcalendar.data.remote.ScheduleService
import com.example.appointmentcalendar.util.TAG
import com.example.appointmentcalendar.util.getLastStartAt
import com.example.appointmentcalendar.util.getNextStartAt
import com.example.appointmentcalendar.util.getStartAt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class ScheduleRepository(
    private val scheduleService: ScheduleService,
    private val teacherScheduleDao: TeacherScheduleDao,
) {
    private val isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    private val snackBarMessage = MutableLiveData<String>()
    private var currentStartedAt :String? = null

    fun getLoadingStatus(): LiveData<Boolean> = isLoading
    fun getSnackBarMessage(): MutableLiveData<String> = snackBarMessage
    fun getTeacherSchedule(): LiveData<TeacherSchedule> = teacherScheduleDao.getTeacherSchedule()
    fun getCurrentStartedAt() = currentStartedAt ?: getStartAt()

    suspend fun refreshData(startedAt: String = getStartAt()) {
        currentStartedAt = startedAt
        isLoading.value = true
        withContext(Dispatchers.IO) {
            try {
                fetchTeacherSchedule(startedAt)
            } catch (exception: Exception) {
                when (exception) {
                    is IOException -> snackBarMessage.postValue("Network problem occurred")
                    else -> {
                        snackBarMessage.postValue("Unexpected problem occurred")
                    }
                }
            }
            isLoading.postValue(false)
        }
    }

    suspend fun getNextWeekData() {
        refreshData(getNextStartAt(getCurrentStartedAt()))
    }

    suspend fun getLastWeekData() {
        refreshData(getLastStartAt(getCurrentStartedAt()))
    }

    private suspend fun fetchTeacherSchedule(startedAt: String) {
        // 未要求老師名，以amy-estrada為例
        val response = scheduleService.getTeacherSchedule("amy-estrada", startedAt)
        if (response.isSuccessful) {
            val teacherSchedule = response.body()
            if (teacherSchedule != null) {
                saveTeacherSchedule(teacherSchedule)
            } else {
                teacherScheduleDao.deleteTeacherSchedule()
            }
        } else {
            snackBarMessage.postValue("Unexpected problem occurred")
            Log.e(TAG, "Error: ${response.errorBody()}")
        }
    }

    private fun saveTeacherSchedule(data: TeacherSchedule) {
        teacherScheduleDao.insertTeacherSchedule(data)
    }
}