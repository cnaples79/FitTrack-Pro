package com.fittrackpro.shared.data

import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.days

class DatabaseCleaner(private val database: FitTrackDatabase) {
    fun cleanOldRecords() {
        val cutoffDate = Clock.System.now().minus(30.days).toEpochMilliseconds()
        
        database.transaction {
            database.workoutQueries.deleteOldWorkouts(cutoffDate)
            database.goalQueries.deleteCompletedGoals(cutoffDate)
        }
    }
}
