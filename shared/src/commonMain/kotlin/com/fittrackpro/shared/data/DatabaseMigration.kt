package com.fittrackpro.shared.data

import app.cash.sqldelight.db.SqlDriver

object DatabaseMigration {
    private const val SCHEMA_VERSION = 1

    fun migrateIfNeeded(driver: SqlDriver) {
        val currentVersion = driver.executeQuery(
            identifier = null,
            sql = "PRAGMA user_version;",
            parameters = 0,
            mapper = { cursor -> 
                if (cursor.next()) {
                    cursor.getLong(0)?.toInt() ?: 0
                } else {
                    0
                }
            }
        ).executeAsOne()

        if (currentVersion < SCHEMA_VERSION) {
            FitTrackDatabase.Schema.create(driver)
            driver.execute(null, "PRAGMA user_version = $SCHEMA_VERSION;", 0)
        }
    }
}
