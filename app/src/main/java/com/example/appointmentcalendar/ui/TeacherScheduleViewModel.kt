package com.example.appointmentcalendar.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appointmentcalendar.data.repository.ScheduleRepository
import com.example.appointmentcalendar.util.getStartAt
import kotlinx.coroutines.launch

class TeacherScheduleViewModel(private val repository: ScheduleRepository) : ViewModel() {
    val teacherSchedule by lazy { repository.getTeacherSchedule() }
    val isLoading by lazy { repository.getLoadingStatus() }
    val snackBarMessage by lazy { repository.getSnackBarMessage() }
    val currentStartedAt get() = repository.getCurrentStartedAt()
    var minStartedAt = getStartAt()
        private set
    fun refreshTeacherSchedule() = viewModelScope.launch { repository.refreshData() }
    fun getNextWeekData() = viewModelScope.launch { repository.getNextWeekData() }
    fun getLastWeekData() = viewModelScope.launch { repository.getLastWeekData() }
    fun clearSnackBar() {
        snackBarMessage.value = ""
    }
    fun checkMinStartedAt(needRefresh: Boolean = false) {
        if (minStartedAt != getStartAt()) {
            minStartedAt = getStartAt()
            if (needRefresh) refreshTeacherSchedule()
        }
    }
}
