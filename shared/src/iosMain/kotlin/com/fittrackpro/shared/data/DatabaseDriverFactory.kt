package com.fittrackpro.shared.data

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.fittrackpro.shared.FitTrackProDatabase

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(FitTrackProDatabase.Schema, "fittrackpro.db")
    }
}
