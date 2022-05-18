package com.example.appointmentcalendar.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.appointmentcalendar.data.local.dao.TeacherScheduleDao
import com.example.appointmentcalendar.data.model.TeacherSchedule
import com.example.appointmentcalendar.data.remote.ScheduleService
import com.example.appointmentcalendar.util.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class ScheduleRepository(
    private val scheduleService: ScheduleService,
    private val teacherScheduleDao: TeacherScheduleDao,
) {
    private val isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    private val snackBarMessage = MutableLiveData<String>()

    fun getLoadingStatus(): LiveData<Boolean> = isLoading
    fun getSnackBarMessage(): MutableLiveData<String> = snackBarMessage
    fun getTeacherSchedule(): LiveData<TeacherSchedule> = teacherScheduleDao.getTeacherSchedule()
    fun clearSnackBar() {
        snackBarMessage.value = ""
    }

    suspend fun refreshData() {
        isLoading.value = true
        withContext(Dispatchers.IO) {
            try {
                //TODO getFirstDayTimeOfWeek
                fetchTeacherSchedule(getFirstDayTimeOfWeek())
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

    private suspend fun fetchTeacherSchedule(startedAt:String) {
        // 未要求老師名，以amy-estrada為例
        val response = scheduleService.getTeacherSchedule("amy-estrada", startedAt)
        if (response.isSuccessful) {
            Log.d(TAG,"Response SUCCESSFUL")
            response.body()?.let {
                saveTeacherSchedule(it)
            }
        } else {
            snackBarMessage.postValue("Unexpected problem occurred")
            Log.e(TAG, "Error: ${response.errorBody()}")
        }
    }

    private fun saveTeacherSchedule(data: TeacherSchedule) {
        teacherScheduleDao.insertTeacherSchedule(data)
    }

    private fun getFirstDayTimeOfWeek(): String {
         val c = Calendar.getInstance(TimeZone.getDefault())
        c[Calendar.DAY_OF_WEEK] = c.firstDayOfWeek
        c[Calendar.HOUR_OF_DAY] = 0
        c[Calendar.MINUTE] = 0
        c[Calendar.SECOND] = 0
        c[Calendar.MILLISECOND] = 0
        Log.d(TAG, "QQ: ${SimpleDateFormat("yyyy-M-dd-HH:mm:ss", Locale.getDefault()).format(c.time)}")
        val df: DateFormat = SimpleDateFormat("yyyy-M-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        df.timeZone = TimeZone.getTimeZone("UTC");
        Log.d(TAG, "QQ: ${df.format(c.time)}")

        getTimeWithTimeZone(df.format(c.time))
        return df.format(c.time)
    }

    private fun getTimeWithTimeZone(time:String){
        var df: DateFormat = SimpleDateFormat("yyyy-M-dd'T'hh:mm:ss.SSS'Z'", Locale.getDefault())
        df.timeZone = TimeZone.getTimeZone("UTC")
        val date = df.parse(time)!!
        df = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault())
        df.setTimeZone(TimeZone.getDefault())
        Log.d(TAG, "QQ: ${df.format(date)}")
    }
}