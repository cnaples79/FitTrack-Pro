package com.fittrackpro.shared.data

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.fittrackpro.shared.FitTrackDatabase

actual class DatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            schema = FitTrackDatabase.Schema,
            context = context,
            name = "fittrack.db"
        )
    }
}
