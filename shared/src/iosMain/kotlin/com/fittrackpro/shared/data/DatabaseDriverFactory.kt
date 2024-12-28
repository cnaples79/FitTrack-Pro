package com.fittrackpro.shared.data

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.fittrackpro.shared.data.FitTrackDatabase

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(
            schema = FitTrackDatabase.Schema,
            name = "fittrack.db"
        )
    }
}
