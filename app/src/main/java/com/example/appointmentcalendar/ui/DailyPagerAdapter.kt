package com.example.appointmentcalendar.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class DailyPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 7

    override fun createFragment(position: Int): Fragment {
        return DailyPagerFragment.newInstance(position)
    }
}
