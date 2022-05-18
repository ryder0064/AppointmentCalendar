package com.example.appointmentcalendar

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.appointmentcalendar.util.TAG
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val viewModel: TeacherScheduleViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.teacherSchedule.observe(this) {
            Log.d(TAG, "teacherSchedule: $it")
        }

        viewModel.isLoading.observe(this) {
            Log.d(TAG, "isLoading: $it")
        }

        viewModel.snackBarMessage.observe(this) { text ->
            Snackbar.make(findViewById(R.id.outlineLayout), text, Snackbar.LENGTH_LONG).setAction(
                getString(R.string.snackbar_action_retry)
            ) {
                viewModel.refreshTeacherSchedule()
            }.show()
            viewModel.clearSnackBar()
        }

        viewModel.refreshTeacherSchedule()
    }
}