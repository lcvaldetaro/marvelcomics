package com.example.comics

import android.app.Application
import android.content.Context

class App : Application() {
    companion object {
        private val TAG = App::class.java.getSimpleName()
        var appContext    : Context? = null
    }

    override fun onCreate() {
        super.onCreate()

        appContext = this

        JavaUtils.setupPicassoCaching(this)
    }
}