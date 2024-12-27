package com.fittrackpro.shared.data

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.fittrackpro.db.FitTrackDatabase

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(
            schema = FitTrackDatabase.Schema,
            name = "fittrack.db"
        )
    }
}
