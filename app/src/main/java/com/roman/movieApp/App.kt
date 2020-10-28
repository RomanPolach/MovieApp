package com.roman.movieApp

import android.app.Application
import com.roman.movieApp.di.remoteModule
import com.roman.movieApp.di.repositoryModule
import com.roman.movieApp.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                repositoryModule,
                remoteModule,
                viewModelModule
            )
        }
    }
}