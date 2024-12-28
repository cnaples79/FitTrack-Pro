package com.fittrackpro.shared.data

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.QueryResult

object DatabaseMigration {
    private const val SCHEMA_VERSION = 1

    fun migrateIfNeeded(driver: SqlDriver) {
        driver.execute(null, "CREATE TABLE IF NOT EXISTS schema_version (version INTEGER NOT NULL);", 0)
        driver.execute(null, "INSERT INTO schema_version (version) SELECT 0 WHERE NOT EXISTS (SELECT 1 FROM schema_version);", 0)
        
        var currentVersion = 0
        val query = driver.executeQuery(
            identifier = null,
            sql = "SELECT version FROM schema_version LIMIT 1;",
            parameters = 0,
            mapper = { cursor -> 
                if (cursor.next()) {
                    currentVersion = cursor.getLong(0)?.toInt() ?: 0
                }
                QueryResult.Value(Unit)
            }
        )
        query.execute()
        query.close()

        if (currentVersion < SCHEMA_VERSION) {
            FitTrackDatabase.Schema.create(driver)
            driver.execute(null, "UPDATE schema_version SET version = $SCHEMA_VERSION;", 0)
        }
    }
}
