package com.fittrackpro.shared.data

import com.squareup.sqldelight.db.SqlDriver

object DatabaseMigration {
    fun migrateIfNeeded(driver: SqlDriver) {
        val oldVersion = driver.executeQuery(
            identifier = null,
            sql = "PRAGMA user_version;",
            mapper = { cursor -> cursor.getLong(0)?.toInt() ?: 0 },
            parameters = 0
        ).value

        val newVersion = 1 // Increment this when adding new migrations

        if (oldVersion < newVersion) {
            driver.execute(null, "BEGIN TRANSACTION;", 0)
            try {
                migrate(driver, oldVersion, newVersion)
                driver.execute(null, "PRAGMA user_version = $newVersion;", 0)
                driver.execute(null, "COMMIT;", 0)
            } catch (e: Exception) {
                driver.execute(null, "ROLLBACK;", 0)
                throw e
            }
        }
    }

    private fun migrate(driver: SqlDriver, oldVersion: Int, newVersion: Int) {
        var version = oldVersion
        while (version < newVersion) {
            version++
            when (version) {
                1 -> migrateV1(driver)
                // Add more migration cases here as needed
            }
        }
    }

    private fun migrateV1(driver: SqlDriver) {
        // Add migration SQL statements here when needed
        // Example:
        // driver.execute(null, "ALTER TABLE workout ADD COLUMN intensity TEXT;", 0)
    }
}
