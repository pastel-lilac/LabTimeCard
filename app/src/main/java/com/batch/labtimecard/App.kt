package com.batch.labtimecard

import android.app.Application
import com.batch.labtimecard.di.Modules.navigatorModule
import com.batch.labtimecard.di.Modules.repositoryModule
import com.batch.labtimecard.di.Modules.useCaseModule
import com.batch.labtimecard.di.Modules.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            // Android context
            androidContext(this@App)
            modules(
                listOf(
                    navigatorModule,
                    repositoryModule,
                    viewModelModule,
                    useCaseModule
                )
            )
        }
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}