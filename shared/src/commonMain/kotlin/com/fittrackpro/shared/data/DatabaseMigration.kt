package com.fittrackpro.shared.data

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver

object DatabaseMigration {
    private const val SCHEMA_VERSION = 1

    fun migrateIfNeeded(driver: SqlDriver) {
        // Create schema_version table if it doesn't exist
        driver.execute(
            identifier = null,
            sql = "CREATE TABLE IF NOT EXISTS schema_version (version INTEGER NOT NULL);",
            parameters = 0,
            binders = null // No binders needed
        )

        // Insert initial version if table is empty
        driver.execute(
            identifier = null,
            sql = "INSERT INTO schema_version (version) SELECT 0 WHERE NOT EXISTS (SELECT 1 FROM schema_version);",
            parameters = 0,
            binders = null // No binders needed
        )

        // Get current version
        val currentVersion = driver.executeQuery(
            identifier = null,
            sql = "SELECT version FROM schema_version LIMIT 1;",
            parameters = 0,
            mapper = { cursor ->
                QueryResult.Value(
                    if (cursor.next().value) {
                        cursor.getLong(0)?.toInt() ?: 0
                    } else {
                        0
                    }
                )
            }
        ).value

        // Compare and update the schema version if needed
        if (currentVersion < SCHEMA_VERSION) {
            FitTrackDatabase.Schema.create(driver)
            driver.execute(
                identifier = null,
                sql = "UPDATE schema_version SET version = $SCHEMA_VERSION;",
                parameters = 0,
                binders = null // No binders needed
            )
        }
    }
}
