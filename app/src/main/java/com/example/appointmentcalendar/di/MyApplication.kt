package com.example.appointmentcalendar.di

import android.app.Application
import org.koin.android.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
            androidFileProperties()
            modules(
                listOf(
                    localDataSourceModule,
                    remoteDataSourceModule,
                    teacherScheduleModule
                )
            )
        }
    }
}
