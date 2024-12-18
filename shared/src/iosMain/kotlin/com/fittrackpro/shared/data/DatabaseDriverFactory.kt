package com.fittrackpro.shared.data

import com.fittrackpro.db.FitTrackDatabase
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(
            schema = FitTrackDatabase.Schema,
            name = "fittrack.db"
        )
    }
}
