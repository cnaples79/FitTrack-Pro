package com.fittrackpro.shared.data

import com.fittrackpro.shared.FitTrackDatabase
import com.fittrackpro.shared.domain.model.GoalStatus
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.days

class DatabaseCleaner(private val database: FitTrackDatabase) {
    suspend fun cleanOldRecords() {
        val thirtyDaysAgo = Clock.System.now().minus(30.days).toEpochMilliseconds()
        
        database.transaction {
            database.workoutQueries.deleteOldWorkouts(thirtyDaysAgo)
            database.goalQueries.deleteOldCompletedGoals(
                status = GoalStatus.COMPLETED.name,
                updated_at = thirtyDaysAgo
            )
        }
    }
}
