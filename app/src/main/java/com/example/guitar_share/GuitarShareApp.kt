package com.example.guitar_share

import android.app.Application
import com.example.guitar_share.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class GuitarShareApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@GuitarShareApp)
            modules(appModule)
        }
    }
}
