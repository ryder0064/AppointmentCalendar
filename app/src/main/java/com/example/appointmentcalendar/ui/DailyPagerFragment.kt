package com.example.appointmentcalendar.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appointmentcalendar.R
import com.example.appointmentcalendar.databinding.FragmentDailyPagerBinding
import com.example.appointmentcalendar.util.getTeacherScheduleInfo
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

private const val ARG_SCHEDULES_POSITION = "ARG_SCHEDULES_POSITION"

class DailyPagerFragment : Fragment() {
    private lateinit var viewAdapter: ScheduleListAdapter
    private var position: Int = 0
    private var _binding: FragmentDailyPagerBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TeacherScheduleViewModel by sharedViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            position = it!!.getInt(ARG_SCHEDULES_POSITION)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDailyPagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewAdapter = ScheduleListAdapter()
        binding.recyclerview.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = viewAdapter
        }
        viewModel.teacherSchedule.observe(viewLifecycleOwner) {
            if (it != null) {
                val teacherScheduleInfo = getTeacherScheduleInfo(viewModel.currentStartedAt, it)
                val scheduleList =
                    teacherScheduleInfo[teacherScheduleInfo.keys.elementAt(position)]!!
                viewAdapter.setData(scheduleList)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(position: Int) =
            DailyPagerFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SCHEDULES_POSITION, position)
                }
            }
    }
}