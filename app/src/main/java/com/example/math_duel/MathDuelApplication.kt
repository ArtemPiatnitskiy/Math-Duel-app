package com.example.math_duel

import android.app.Application
import com.example.math_duel.di.modules.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MathDuelApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Инициализируем Koin
        startKoin {
            androidContext(this@MathDuelApplication)
            modules(appModule)
        }
    }
}

