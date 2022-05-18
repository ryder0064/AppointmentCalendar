package com.example.appointmentcalendar

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appointmentcalendar.data.repository.ScheduleRepository
import kotlinx.coroutines.launch

class TeacherScheduleViewModel(private val repository: ScheduleRepository) : ViewModel() {
    val teacherSchedule by lazy { repository.getTeacherSchedule() }
    val isLoading by lazy { repository.getLoadingStatus() }
    val snackBarMessage: MutableLiveData<String> = repository.getSnackBarMessage()

    fun refreshTeacherSchedule() = viewModelScope.launch { repository.refreshData() }
    fun clearSnackBar() {
        repository.clearSnackBar()
    }
}
