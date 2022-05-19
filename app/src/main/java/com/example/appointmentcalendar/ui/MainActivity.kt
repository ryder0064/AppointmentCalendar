package com.example.appointmentcalendar.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.appointmentcalendar.R
import com.example.appointmentcalendar.databinding.ActivityMainBinding
import com.example.appointmentcalendar.util.getFormatTimeFromTimeLong
import com.example.appointmentcalendar.util.getTeacherScheduleInfo
import com.example.appointmentcalendar.util.getWeekIntervalMessage
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private val viewModel: TeacherScheduleViewModel by viewModel()
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: DailyPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.nextBtn.setOnClickListener {
            viewModel.getNextWeekData()
            refreshWeekSelectionLayout()
        }
        binding.lastBtn.setOnClickListener {
            viewModel.getLastWeekData()
            refreshWeekSelectionLayout()
        }

        refreshWeekSelectionLayout()
        adapter = DailyPagerAdapter(this)
        binding.viewPager.adapter = adapter

        viewModel.teacherSchedule.observe(this) {
            if (it != null) {
                val teacherScheduleInfo = getTeacherScheduleInfo(viewModel.currentStartedAt, it)
                TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                    tab.text = getFormatTimeFromTimeLong(
                        teacherScheduleInfo.keys.elementAt(position),
                        SimpleDateFormat("EEE, MMM dd", Locale.getDefault())
                    )
                }.attach()
            }
        }

        viewModel.isLoading.observe(this) {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        viewModel.snackBarMessage.observe(this) { text ->
            val rootView: View =
                window.decorView.findViewById(android.R.id.content)
            Snackbar.make(rootView, text, Snackbar.LENGTH_LONG).setAction(
                getString(R.string.snackbar_action_retry)
            ) {
                viewModel.refreshTeacherSchedule()
            }.show()
        }

        viewModel.refreshTeacherSchedule()
    }

    override fun onResume() {
        super.onResume()
        refreshWeekSelectionLayout(true)
    }

    private fun refreshWeekSelectionLayout(needRefresh: Boolean = false) {
        viewModel.checkMinStartedAt(needRefresh)
        if (viewModel.minStartedAt == viewModel.currentStartedAt) {
            binding.lastBtn.visibility = View.INVISIBLE
        } else {
            binding.lastBtn.visibility = View.VISIBLE
        }
        binding.interval.text = getWeekIntervalMessage(viewModel.currentStartedAt)
    }
}