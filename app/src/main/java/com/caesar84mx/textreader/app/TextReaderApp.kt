package com.caesar84mx.textreader.app

import android.app.Application
import com.caesar84mx.textreader.di.module
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TextReaderApp: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@TextReaderApp)
            modules(module)
        }
    }
}