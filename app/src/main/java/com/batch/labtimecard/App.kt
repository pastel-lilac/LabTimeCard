package com.batch.labtimecard

import android.app.Application
import com.batch.labtimecard.di.Modules.apiModule
import com.batch.labtimecard.di.Modules.navigatorModule
import com.batch.labtimecard.di.Modules.repositoryModule
import com.batch.labtimecard.di.Modules.useCaseModule
import com.batch.labtimecard.di.Modules.viewModelModule
import com.facebook.stetho.Stetho
import com.jakewharton.threetenabp.AndroidThreeTen
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
                    useCaseModule,
                    apiModule
                )
            )
        }
        AndroidThreeTen.init(this)
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
            Timber.plant(Timber.DebugTree())
        }
    }
}