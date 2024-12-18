package com.fittrackpro.shared.data

import android.content.Context
import com.fittrackpro.db.FitTrackDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver

actual class DatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            schema = FitTrackDatabase.Schema,
            context = context,
            name = "fittrack.db"
        )
    }
}
