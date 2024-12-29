package com.fittrackpro.shared.data

import com.fittrackpro.shared.FitTrackDatabase
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.days

class DatabaseCleaner(private val database: FitTrackDatabase) {
    suspend fun cleanOldRecords() {
        val thirtyDaysAgo = Clock.System.now().minus(30.days).toEpochMilliseconds()
        
        database.transaction {
            database.workoutQueries.deleteOldWorkouts(thirtyDaysAgo)
            database.goalQueries.deleteExpiredGoals(thirtyDaysAgo)
        }
    }
}
