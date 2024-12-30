package com.fittrackpro.android

import android.app.Application
import android.content.Context
import com.fittrackpro.shared.data.DatabaseDriverFactory
import com.fittrackpro.shared.di.initKoin
import com.fittrackpro.shared.util.AndroidDispatchers

class FitTrackProApp : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        
        // Initialize Koin with default test user
        initKoin(
            databaseDriverFactory = DatabaseDriverFactory(this),
            dispatchers = AndroidDispatchers(),
            userId = TEST_USER_ID
        ) {}
    }

    companion object {
        lateinit var appContext: Context
            private set
            
        // Default test user ID - we'll replace this with proper auth later
        const val TEST_USER_ID = 1L
    }
}
