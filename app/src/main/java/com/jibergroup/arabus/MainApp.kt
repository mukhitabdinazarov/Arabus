package com.jibergroup.arabus

import android.app.Application
import com.jibergroup.arabus.di.appModule
import com.jibergroup.arabus.di.uiModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MainApp)
            modules(listOf(appModule, uiModule))
        }
    }
}