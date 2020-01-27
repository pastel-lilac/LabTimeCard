package com.batch.labtimecard

import android.app.Application
import com.batch.labtimecard.di.Modules.navigatorModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            // Android context
            androidContext(this@App)
            modules(listOf(navigatorModule))
        }
    }
}