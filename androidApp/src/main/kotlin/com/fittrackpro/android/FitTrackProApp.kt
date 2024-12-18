package com.fittrackpro.android

import android.app.Application
import android.content.Context

class FitTrackProApp : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }

    companion object {
        lateinit var appContext: Context
            private set
    }
}
