package com.example.appointmentcalendar.di

import androidx.room.Room
import com.example.appointmentcalendar.ui.TeacherScheduleViewModel
import com.example.appointmentcalendar.data.local.TeacherScheduleDatabase
import com.example.appointmentcalendar.data.remote.ScheduleService
import com.example.appointmentcalendar.data.repository.ScheduleRepository
import com.example.appointmentcalendar.util.BASE_URL
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val localDataSourceModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            TeacherScheduleDatabase::class.java,
            "teacher_schedule.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
    single { get<TeacherScheduleDatabase>().teacherScheduleDao() }
}

val remoteDataSourceModule = module {
    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    single { get<Retrofit>().create(ScheduleService::class.java) }
}

val teacherScheduleModule = module {
    single {
        ScheduleRepository(get(), get())
    }
    viewModel { TeacherScheduleViewModel(get()) }
}