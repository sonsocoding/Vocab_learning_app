package com.example.vocablearningapp

import android.app.Application

class VocabApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
