package com.fittrackpro.shared.data

import com.fittrackpro.db.FitTrackDatabase
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime

class DatabaseCleaner(private val database: FitTrackDatabase) {
    fun cleanOldData() {
        val threeMonthsAgo = Clock.System.now()
            .minus(90, DateTimeUnit.DAY)
            .toLocalDateTime()
            .date
            .toString()

        database.transaction {
            // Delete old completed workouts
            database.workoutQueries.deleteOldWorkouts(threeMonthsAgo)
            
            // Delete old completed goals
            database.goalQueries.deleteOldCompletedGoals(threeMonthsAgo)
        }
    }
}
